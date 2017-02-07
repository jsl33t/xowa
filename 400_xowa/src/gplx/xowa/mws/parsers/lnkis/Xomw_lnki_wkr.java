/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012 gnosygnu@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package gplx.xowa.mws.parsers.lnkis; import gplx.*; import gplx.xowa.*; import gplx.xowa.mws.*; import gplx.xowa.mws.parsers.*;
import gplx.core.btries.*; import gplx.core.primitives.*;
import gplx.langs.phps.utls.*;
import gplx.xowa.wikis.nss.*; import gplx.xowa.wikis.xwikis.*;
import gplx.xowa.mws.parsers.*; import gplx.xowa.mws.parsers.quotes.*;
import gplx.xowa.mws.htmls.*; import gplx.xowa.mws.linkers.*;
import gplx.xowa.mws.utls.*; import gplx.xowa.mws.libs.*;
import gplx.xowa.mws.media.*; import gplx.xowa.mws.filerepo.file.*;
import gplx.xowa.parsers.uniqs.*;
/*	TODO.XO
	* P7: multi-line links; // look at the next 'line' to see if we can close it there
	* P7: interwiki
	* P7: [[File:]]
	* P7: [[Category:]]
	* P6: [[Media:]]
	* P4: handle "]]]"; "If we get a ] at the beginning of $m[3]"
	* P4: handle "[[http://a.org]]"
	* P3: $langObj->formatNum( ++$this->mAutonumber );
	* P2: $this->getConverterLanguage()->markNoConversion( $text );
	* P1: link_prefix; EX: b[[A]]; [not enabled on enwiki]
*/
public class Xomw_lnki_wkr {// THREAD.UNSAFE: caching for repeated calls
	private final    Xomw_link_holders holders;
	private final    Xomw_linker linker;
	private final    Xomw_link_renderer link_renderer;
	// private final    Btrie_slim_mgr protocols_trie;
	private final    Xomw_quote_wkr quote_wkr;
	private final    Xomw_strip_state strip_state;
	private Xomw_parser_env env;
	private Xow_wiki wiki;
	private Xoa_ttl page_title;
	private final    Xomw_linker__normalize_subpage_link normalize_subpage_link = new Xomw_linker__normalize_subpage_link();
	private final    Bry_bfr tmp;
	private final    Xomw_parser parser;
	private final    Xomw_atr_mgr extra_atrs = new Xomw_atr_mgr();
	private final    Xomw_qry_mgr query = new Xomw_qry_mgr();
	private final    Btrie_rv trv = new Btrie_rv();
	private final    List_adp tmp_list = List_adp_.New();
	private final    Hash_adp mImageParams = Hash_adp_bry.cs();
	private final    Hash_adp mImageParamsMagicArray = Hash_adp_bry.cs();
	public Xomw_lnki_wkr(Xomw_parser parser, Xomw_link_holders holders, Xomw_link_renderer link_renderer, Btrie_slim_mgr protocols_trie) {
		this.parser = parser;
		this.holders = holders;
		this.link_renderer = link_renderer;
		// this.protocols_trie = protocols_trie;

		this.linker = parser.Linker();
		this.quote_wkr = parser.Quote_wkr();
		this.tmp = parser.Tmp();
		this.strip_state = parser.Strip_state();
	}
	public void Init_by_wiki(Xomw_parser_env env, Xow_wiki wiki) {
		this.env = env;
		this.wiki = wiki;
		if (title_chars_for_lnki == null) {
			title_chars_for_lnki = (boolean[])Array_.Clone(Xomw_ttl_utl.Title_chars_valid());
			// the % is needed to support urlencoded titles as well
			title_chars_for_lnki[Byte_ascii.Hash] = true;
			title_chars_for_lnki[Byte_ascii.Percent] = true;
		}
	}
	public void Clear_state() {
		holders.Clear();
	}
	public void Replace_internal_links(Xomw_parser_ctx pctx, Xomw_parser_bfr pbfr) {
		// XO.PBFR
		Bry_bfr src_bfr = pbfr.Src();
		byte[] src = src_bfr.Bfr();
		int src_bgn = 0;
		int src_end = src_bfr.Len();
		Bry_bfr bfr = pbfr.Trg();
		pbfr.Switch();

		this.page_title = pctx.Page_title();

		Replace_internal_links(pctx, bfr, src, src_bgn, src_end);
	}
	// XO.MW:SYNC:1.29; DATE:2017-02-02
	public void Replace_internal_links(Xomw_parser_ctx pctx, Bry_bfr bfr, byte[] src, int src_bgn, int src_end) {
		// XO.MW: regex for tc move to header; e1 and e1_img moved to code
		// the % is needed to support urlencoded titles as well

		// XO.MW.BGN: split the entire text String on occurrences of [[
		int cur = src_bgn;
		int prv = cur;
		while (true) {
			int lnki_bgn = Bry_find_.Find_fwd(src, Bry__wtxt__lnki__bgn, cur, src_end);	// $a = StringUtils::explode('[[', ' ' . $s);
			if (lnki_bgn == Bry_find_.Not_found) {	// no more "[["; stop loop
				bfr.Add_mid(src, cur, src_end);
				break;
			}
			cur = lnki_bgn + 2;	// 2="[[".length

			// XO.MW.IGNORE: handles strange split logic of adding space to String; "$s = substr($s, 1);"

			// TODO.XO:link_prefix; EX: b[[A]]
			// $useLinkPrefixExtension = $this->getTargetLanguage()->linkPrefixExtension();
			// $e2 = null;
			// if ($useLinkPrefixExtension) {
			// 	// Match the end of a line for a word that's not followed by whitespace,
			// 	// e.g. in the case of 'The Arab al[[Razi]]', 'al' will be matched
			// 	global $wgContLang;
			// 	$charset = $wgContLang->linkPrefixCharset();
			// 	$e2 = "/^((?>.*[^$charset]|))(.+)$/sDu";
			// }

			// IGNORE: throw new MWException(__METHOD__ . ": \$this->mTitle is null\n");

			// $nottalk = !$this->mTitle->isTalkPage();

			// TODO.XO:link_prefix
			byte[] prefix = Bry_.Empty;
			//if ($useLinkPrefixExtension) {
			//	$m = [];
			//	if (preg_match($e2, $s, $m)) {
			//		$first_prefix = $m[2];
			//	} else {
			//		$first_prefix = false;
			//	}
			//} else {
			//	$prefix = '';
			//}

			// TODO.XO:link_prefix; EX: b[[A]]
			//if ($useLinkPrefixExtension) {
			//	if (preg_match($e2, $s, $m)) {
			//		$prefix = $m[2];
			//		$s = $m[1];
			//	} else {
			//		$prefix = '';
			//	}
			//	// first link
			//	if ($first_prefix) {
			//		$prefix = $first_prefix;
			//		$first_prefix = false;
			//	}
			//}

			// PORTED.BGN: if (preg_match($e1, $line, $m)) && else if (preg_match($e1_img, $line, $m))
			// NOTE: both e1 and e1_img are effectively the same; e1_img allows nested "[["; EX: "[[A|b[[c]]d]]" will stop at "[[A|b"
			int ttl_bgn = cur;
			int ttl_end = Xomw_ttl_utl.Find_fwd_while_title(src, cur, src_end, title_chars_for_lnki);
			cur = ttl_end;
			int capt_bgn = -1, capt_end = -1;
			int nxt_lnki = -1;

			boolean might_be_img = false;
			if (ttl_end > ttl_bgn) {	// at least one valid title-char found; check for "|" or "]]" EX: "[[a"
				byte nxt_byte = src[ttl_end];
				if      (nxt_byte == Byte_ascii.Pipe) {	// handles lnki with capt ([[A|a]])and lnki with file ([[File:A.png|b|c|d]])
					cur = ttl_end + 1;

					// find next "[["
					nxt_lnki = Bry_find_.Find_fwd(src, Bry__wtxt__lnki__bgn, cur, src_end);
					if (nxt_lnki == Bry_find_.Not_found)
						nxt_lnki = src_end;

					// find end "]]"
					capt_bgn = cur;
					capt_end = Bry_find_.Find_fwd(src, Bry__wtxt__lnki__end, cur, nxt_lnki);
					if (capt_end == Bry_find_.Not_found) {
						capt_end = nxt_lnki;
						cur = nxt_lnki;
						might_be_img = true;
					}
					else {
						cur = capt_end + Bry__wtxt__lnki__end.length;
					}
				}
				else if (Bry_.Match(src, ttl_end, ttl_end + 2, Bry__wtxt__lnki__end)) {	// handles simple lnki; EX: [[A]]
					cur = ttl_end + 2;
				}
				else {
					ttl_end = -1;
				}
			}
			else
				ttl_end = -1;
			if (ttl_end == -1) { // either (a) no valid title-chars ("[[<") or (b) title char, but has stray "]" ("[[a]b]]")
				// Invalid form; output directly
				bfr.Add_mid(src, prv, lnki_bgn + 2);
				bfr.Add_mid(src, cur, ttl_bgn);
				prv = cur = ttl_bgn;
				continue;
			}
			// PORTED.END: if (preg_match($e1, $line, $m)) && else if (preg_match($e1_img, $line, $m))

			byte[] text = Bry_.Mid(src, capt_bgn, capt_end);
			byte[] trail = Bry_.Empty;
			if (!might_be_img) {
				// TODO.XO:
				// If we get a ] at the beginning of $m[3] that means we have a link that's something like:
				// [[Image:Foo.jpg|[http://example.com desc]]] <- having three ] in a row fucks up,
				// the real problem is with the $e1 regex
				// See T1500.
				// Still some problems for cases where the ] is meant to be outside punctuation,
				// and no image is in sight. See T4095.
//					if ($text !== ''
//						&& substr($m[3], 0, 1) === ']'
//						&& strpos($text, '[') !== false
//					) {
//						$text .= ']'; // so that replaceExternalLinks($text) works later
//						$m[3] = substr($m[3], 1);
//					}

				// fix up urlencoded title texts
//					if (strpos($m[1], '%') !== false) {
//						// Should anchors '#' also be rejected?
//						$m[1] = str_replace([ '<', '>' ], [ '&lt;', '&gt;' ], rawurldecode($m[1]));
//					}
//					$trail = $m[3];
			} 
			else {
				// Invalid, but might be an image with a link in its caption
//					$text = $m[2];
//					if (strpos($m[1], '%') !== false) {
//						$m[1] = str_replace([ '<', '>' ], [ '&lt;', '&gt;' ], rawurldecode($m[1]));
//					}
//					$trail = "";
			}

			byte[] orig_link = Bry_.Mid(src, ttl_bgn, ttl_end);

			// TODO.XO: handle "[[http://a.org]]"
			// Don't allow @gplx.Internal protected links to pages containing
			// PROTO: where PROTO is a valid URL protocol; these
			// should be external links.
			// if (preg_match('/^(?i:' . $this->mUrlProtocols . ')/', $origLink)) {
			// 	$s .= $prefix . '[[' . $line;
			//	continue;
			// }

			byte[] link = orig_link;
			boolean no_force = orig_link[0] != Byte_ascii.Colon;
			if (!no_force) {
				// Strip off leading ':'
				link = Bry_.Mid(link, 1);
			}
			Xoa_ttl nt = wiki.Ttl_parse(link);

			// Make subpage if necessary
			boolean subpages_enabled = nt.Ns().Subpages_enabled();
			if (subpages_enabled) {
				Maybe_do_subpage_link(normalize_subpage_link, orig_link, text);
				link = normalize_subpage_link.link;
				text = normalize_subpage_link.text;
				nt = wiki.Ttl_parse(link);
			}
			// IGNORE: handled in rewrite above
			// else {
			//	link = orig_link;
			// }

			byte[] unstrip = strip_state.Unstrip_nowiki(link);
			if (!Bry_.Eq(unstrip, link))
				nt = wiki.Ttl_parse(unstrip);
			if (nt == null) {
				bfr.Add_mid(src, prv, lnki_bgn + 2);	// $s .= $prefix . '[[' . $line;
				prv = cur = lnki_bgn + 2;					
				continue;
			}

			Xow_ns ns = nt.Ns();
			Xow_xwiki_itm iw = nt.Wik_itm();

			if (might_be_img) { // if this is actually an invalid link
				if (ns.Id_is_file() && no_force) { // but might be an image
					boolean found = false;
//						while (true) {
//							// look at the next 'line' to see if we can close it there
//							a->next();
//							next_line = a->current();
//							if (next_line === false || next_line === null) {
//								break;
//							}
//							m = explode(']]', next_line, 3);
//							if (count(m) == 3) {
//								// the first ]] closes the inner link, the second the image
//								found = true;
//								text .= "[[{m[0]}]]{m[1]}";
//								trail = m[2];
//								break;
//							} else if (count(m) == 2) {
//								// if there's exactly one ]] that's fine, we'll keep looking
//								text .= "[[{m[0]}]]{m[1]}";
//							} else {
//								// if next_line is invalid too, we need look no further
//								text .= '[[' . next_line;
//								break;
//							}
//						}
					if (!found) {
						// we couldn't find the end of this imageLink, so output it raw
						// but don't ignore what might be perfectly normal links in the text we've examined
						Bry_bfr nested = wiki.Utl__bfr_mkr().Get_b128();
						this.Replace_internal_links(pctx, nested, text, 0, text.length);
						nested.Mkr_rls();
						bfr.Add(prefix).Add(Bry__wtxt__lnki__bgn).Add(link).Add_byte_pipe().Add(text); // s .= "{prefix}[[link|text";
						// note: no trail, because without an end, there *is* no trail
						continue;
					}
				}
				else { // it's not an image, so output it raw
					bfr.Add(prefix).Add(Bry__wtxt__lnki__bgn).Add(link).Add_byte_pipe().Add(text); // s .= "{prefix}[[link|text";
					// note: no trail, because without an end, there *is* no trail
					continue;
				}
			}

			boolean was_blank = text.length == 0;
			if (was_blank) {
				text = link;
			} 
			else {
				// T6598 madness. Handle the quotes only if they come from the alternate part
				// [[Lista d''e paise d''o munno]] -> <a href="...">Lista d''e paise d''o munno</a>
				// [[Criticism of Harry Potter|Criticism of ''Harry Potter'']]
				//    -> <a href="Criticism of Harry Potter">Criticism of <i>Harry Potter</i></a>
				text = quote_wkr.Do_quotes(tmp, text);
			}

			// Link not escaped by : , create the various objects
//				if (no_force && !nt->wasLocalInterwiki()) {
				// Interwikis
//					if (
//						iw && this->mOptions->getInterwikiMagic() && nottalk && (
//							Language::fetchLanguageName(iw, null, 'mw') ||
//							in_array(iw, wgExtraInterlanguageLinkPrefixes)
//						)
//					) {
					// T26502: filter duplicates
//						if (!isset(this->mLangLinkLanguages[iw])) {
//							this->mLangLinkLanguages[iw] = true;
//							this->mOutput->addLanguageLink(nt->getFullText());
//						}
//
//						s = rtrim(s . prefix);
//						s .= trim(trail, "\n") == '' ? '': prefix . trail;
//						continue;
//					}
//
				if (ns.Id_is_file()) {
//						boolean is_good_image = !wfIsBadImage(nt->getDBkey(), this->mTitle)
					boolean is_good_image = true;
					if (is_good_image) {
						if (was_blank) {
							// if no parameters were passed, text
							// becomes something like "File:Foo.png",
							// which we don't want to pass on to the
							// image generator
							text = Bry_.Empty;
						}
						else {
							// recursively parse links inside the image caption
							// actually, this will parse them in any other parameters, too,
							// but it might be hard to fix that, and it doesn't matter ATM
//								text = this->replaceExternalLinks(text);
//								holders->merge(this->replaceInternalLinks2(text));
						}
						// cloak any absolute URLs inside the image markup, so replaceExternalLinks() won't touch them
						bfr.Add(prefix);
						// Armor_links(Make_image(bfr, nt, text, holders))
						this.makeImage(pctx, bfr, nt, text, holders);
						bfr.Add(trail);
						continue;
					}
				} 
				else if (ns.Id_is_ctg()) {
					bfr.Trim_end_ws(); // s = rtrim(s . "\n"); // T2087

					if (was_blank) {
//							sortkey = this->getDefaultSort();
					}
					else {
//							sortkey = text;
					}
//						sortkey = Sanitizer::decodeCharReferences(sortkey);
//						sortkey = str_replace("\n", '', sortkey);
//						sortkey = this->getConverterLanguage()->convertCategoryKey(sortkey);
//						this->mOutput->addCategory(nt->getDBkey(), sortkey);
//
					// Strip the whitespace Category links produce, see T2087
//						s .= trim(prefix . trail, "\n") == '' ? '' : prefix . trail;

					continue;
				}
//				}

			// Self-link checking. For some languages, variants of the title are checked in
			// LinkHolderArray::doVariants() to allow batching the existence checks necessary
			// for linking to a different variant.
			if (!ns.Id_is_special() && nt.Eq_full_db(page_title) && !nt.Has_fragment()) {
				bfr.Add(prefix);
				linker.Make_self_link_obj(bfr, nt, text, Bry_.Empty, trail, Bry_.Empty);
				continue;
			}

			// NS_MEDIA is a pseudo-namespace for linking directly to a file
			// @todo FIXME: Should do batch file existence checks, see comment below
			if (ns.Id_is_media()) {
				// Give extensions a chance to select the file revision for us
//					options = [];
//					desc_query = false;
				// MW.HOOK:BeforeParserFetchFileAndTitle
				// Fetch and register the file (file title may be different via hooks)
//					list(file, nt) = this->fetchFileAndTitle(nt, options);
				// Cloak with NOPARSE to avoid replacement in replaceExternalLinks
//					s .= prefix . this->armorLinks(
//						Linker::makeMediaLinkFile(nt, file, text)) . trail;
//					continue;
			}

			// Some titles, such as valid special pages or files in foreign repos, should
			// be shown as bluelinks even though they're not included in the page table
			// @todo FIXME: isAlwaysKnown() can be expensive for file links; we should really do
			// batch file existence checks for NS_FILE and NS_MEDIA
			bfr.Add_mid(src, prv, lnki_bgn);
			prv = cur;
			if (iw == null && nt.Is_always_known()) {
				// this->mOutput->addLink(nt);
				Make_known_link_holder(bfr, nt, text, trail, prefix);
			}
			else {
				// Links will be added to the output link list after checking
				holders.Make_holder(bfr, nt, text, Bry_.Ary_empty, trail, prefix);
			}
		}
	}
	public void makeImage(Xomw_parser_ctx pctx, Bry_bfr bfr, Xoa_ttl title, byte[] options_at_link, Xomw_link_holders holders) {
		// Check if the options text is of the form "options|alt text"
		// Options are:
		//  * thumbnail  make a thumbnail with enlarge-icon and caption, alignment depends on lang
		//  * left       no resizing, just left align. label is used for alt= only
		//  * right      same, but right aligned
		//  * none       same, but not aligned
		//  * ___px      scale to ___ pixels width, no aligning. e.g. use in taxobox
		//  * center     center the image
		//  * frame      Keep original image size, no magnify-button.
		//  * framed     Same as "frame"
		//  * frameless  like 'thumb' but without a frame. Keeps user preferences for width
		//  * upright    reduce width for upright images, rounded to full __0 px
		//  * border     draw a 1px border around the image
		//  * alt        Text for HTML alt attribute (defaults to empty)
		//  * class      Set a class for img node
		//  * link       Set the target of the image link. Can be external, interwiki, or local
		// vertical-align values (no % or length right now):
		//  * baseline
		//  * sub
		//  * super
		//  * top
		//  * text-top
		//  * middle
		//  * bottom
		//  * text-bottom

		// Protect LanguageConverter markup when splitting into parts
		byte[][] parts = Xomw_string_utils.Delimiter_explode(tmp_list, trv, options_at_link);

		// Give extensions a chance to select the file revision for us
//			$options = [];
		byte[] desc_query = null;
		// XO.MW.HOOK:BeforeParserFetchFileAndTitle

		// Fetch and register the file (file title may be different via hooks)
//			list($file, $title) = $this->fetchFileAndTitle($title, $options);
		Xomw_File file = fetchFileAndTitle(title, null);

		// Get parameter map
		Xomw_MediaHandler handler = file == null ? null : file.getHandler(env);

		Xomw_image_params tmp_img_params = pctx.Lnki_wkr__make_image__img_params;
		this.getImageParams(tmp_img_params, handler);
		Xomw_param_map paramMap = tmp_img_params.paramMap;
		Xomw_MagicWordArray mwArray = tmp_img_params.mwArray;

		// XO.MW.UNSUPPORTED.TrackingCategory: if (!$file) $this->addTrackingCategory('broken-file-category');

		// Process the input parameters
		byte[] caption = Bry_.Empty;
		// XO.MW: $params = [ 'frame' => [], 'handler' => [], 'horizAlign' => [], 'vertAlign' => [] ];
		Xomw_params_frame       frameParams = paramMap.Frame.Clear();
		Xomw_params_handler     handlerParams = paramMap.Handler.Clear();
//			Xomw_params_horizAlign  horizAlignParams = paramMap.HorizAlign.Clear();
//			Xomw_params_vertAlign   vertAlignParams = paramMap.VertAlign.Clear();
		boolean seen_format = false;

		int parts_len = parts.length;
		for (int i = 0; i < parts_len; i++) {
			byte[] part = parts[i];
			part = Bry_.Trim(part);
			byte[][] tmp_match_word = pctx.Lnki_wkr__make_image__match_magic_word;
			mwArray.matchVariableStartToEnd(tmp_match_word, part);
			byte[] magic_name = tmp_match_word[0];
			byte[] val        = tmp_match_word[1];
			boolean validated = false;
			
			Xomw_param_itm param_item = paramMap.Get_by(magic_name);
			if (param_item != null) {
				int typeUid = param_item.type_uid;
				int paramNameUid = param_item.name_uid;
				// Special case; width and height come in one variable together
				if (typeUid == Xomw_param_map.Type__handler && paramNameUid == Xomw_param_itm.Name__width) {
					int[] tmp_img_size = pctx.Lnki_wkr__make_image__img_size;
					this.parseWidthParam(tmp_img_size, val);
					int parsedW = tmp_img_size[0];
					int parsedH = tmp_img_size[1];
					if (parsedW != 0) {
						if (handler.validateParam(Xomw_param_itm.Name__width, null, parsedW)) {
							paramMap.Set(typeUid, Xomw_param_itm.Name__width, null, parsedW);
							validated = true;
						}
					}
					if (parsedH != 0) {
						if (handler.validateParam(Xomw_param_itm.Name__height, null, parsedH)) {
							paramMap.Set(typeUid, Xomw_param_itm.Name__height, null, parsedH);
							validated = true;
						}
					}
					// else no validation -- T15436
				}
				else {
					if (typeUid == Xomw_param_map.Type__handler) {
						// Validate handler parameter
						// validated = $handler->validateParam($paramName, $value);
					}
					else {
						// Validate @gplx.Internal protected parameters
						switch (paramNameUid) {
							case Xomw_param_itm.Name__manual_thumb:
							case Xomw_param_itm.Name__alt:
							case Xomw_param_itm.Name__class:
								// @todo FIXME: Possibly check validity here for
								// manualthumb? downstream behavior seems odd with
								// missing manual thumbs.
								validated = true;
								// $value = $this->stripAltText($value, $holders);
								break;
							case Xomw_param_itm.Name__link:
//									$chars = self::EXT_LINK_URL_CLASS;
//									$addr = self::EXT_LINK_ADDR;
//									$prots = $this->mUrlProtocols;
//									if ($value === '') {
//										$paramName = 'no-link';
//										$value = true;
									validated = true;
//									}
//									else if (preg_match("/^((?i)$prots)/", $value)) {
//										if (preg_match("/^((?i)$prots)$addr$chars*$/u", $value, $m)) {
//											$paramName = 'link-url';
//											$this->mOutput->addExternalLink($value);
//											if ($this->mOptions->getExternalLinkTarget()) {
//												$params[$type]['link-target'] = $this->mOptions->getExternalLinkTarget();
//											}
										validated = true;
//										}
//									} else {
//										$linkTitle = Title::newFromText($value);
//										if ($linkTitle) {
//											$paramName = 'link-title';
//											$value = $linkTitle;
//											$this->mOutput->addLink($linkTitle);
										validated = true;
//										}
//									}
								break;
							case Xomw_param_itm.Name__frameless:
							case Xomw_param_itm.Name__framed:
							case Xomw_param_itm.Name__thumbnail:
								// use first appearing option, discard others.
								validated = !seen_format;
								seen_format = true;
								break;
							default:
								// Most other things appear to be empty or numeric...
								validated = (val == null || Php_utl_.isnumeric(Bry_.Trim(val)));
								break;
						}
					}
					if (validated) {
						paramMap.Set(typeUid, paramNameUid, val, -1);
					}
				}
			}
			if (!validated) {
				caption = part;
			}
		}

		// Process alignment parameters
		Xomw_param_itm tmp = paramMap.Get_by(Xomw_param_map.Type__horizAlign);
		if (tmp != null) {
//				frameParams.align = tmp.val;
		}
		tmp = paramMap.Get_by(Xomw_param_map.Type__vertAlign);
		if (tmp != null) {
//				frameParams.valign = tmp.val;
		}

		frameParams.caption = caption;

		boolean image_is_framed 
			=  frameParams.frame != null
			|| frameParams.framed != null
			|| frameParams.thumbnail != null
			|| frameParams.manual_thumb != null
			;

		// Will the image be presented in a frame, with the caption below?
		// In the old days, [[Image:Foo|text...]] would set alt text.  Later it
		// came to also set the caption, ordinary text after the image -- which
		// makes no sense, because that just repeats the text multiple times in
		// screen readers.  It *also* came to set the title attribute.
		// Now that we have an alt attribute, we should not set the alt text to
		// equal the caption: that's worse than useless, it just repeats the
		// text.  This is the framed/thumbnail case.  If there's no caption, we
		// use the unnamed parameter for alt text as well, just for the time be-
		// ing, if the unnamed param is set and the alt param is not.
		// For the future, we need to figure out if we want to tweak this more,
		// e.g., introducing a title= parameter for the title; ignoring the un-
		// named parameter entirely for images without a caption; adding an ex-
		// plicit caption= parameter and preserving the old magic unnamed para-
		// meter for BC; ...
		if (image_is_framed) { // Framed image
			if (caption == Bry_.Empty && frameParams.alt == null) {
				// No caption or alt text, add the filename as the alt text so
				// that screen readers at least get some description of the image
				frameParams.alt = title.Get_text();
			}
			// Do not set $params['frame']['title'] because tooltips don't make sense
			// for framed images
		} 
		else { // Inline image
			if (frameParams.alt == null) {
				// No alt text, use the "caption" for the alt text
				if (caption != Bry_.Empty) {
//						frameParams.alt = $this->stripAltText(caption, $holders);
				}
				else {
					// No caption, fall back to using the filename for the
					// alt text
					frameParams.alt = title.Get_text();
				}
			}
			// Use the "caption" for the tooltip text
//				frameParams.title = $this->stripAltText(caption, $holders);
		}

		// MW.HOOK:ParserMakeImageParams

		// Linker does the rest
//			byte[] time = options.time;
		Object time = null;
//			options = $this->mOptions->getThumbSize()
		linker.makeImageLink(bfr, parser, title, file, frameParams, handlerParams, time, desc_query, null);

		// Give the handler a chance to modify the parser Object
//			if (handler != null) {
//				$handler->parserTransformHook($this, $file);
//			}
	}
//		protected function stripAltText( $caption, $holders ) {
//			// Strip bad stuff out of the title (tooltip).  We can't just use
//			// replaceLinkHoldersText() here, because if this function is called
//			// from replaceInternalLinks2(), mLinkHolders won't be up-to-date.
//			if ( $holders ) {
//				$tooltip = $holders->replaceText( $caption );
//			} else {
//				$tooltip = $this->replaceLinkHoldersText( $caption );
//			}
//
//			// make sure there are no placeholders in thumbnail attributes
//			// that are later expanded to html- so expand them now and
//			// remove the tags
//			$tooltip = $this->mStripState->unstripBoth( $tooltip );
//			$tooltip = Sanitizer::stripAllTags( $tooltip );
//
//			return $tooltip;
//		}

	private static Xomw_param_list[] internalParamNames;
	private static Xomw_param_map internalParamMap;

	private void getImageParams(Xomw_image_params rv, Xomw_MediaHandler handler) {
		byte[] handlerClass = handler == null ? Bry_.Empty : handler.Key();
		rv.paramMap = (Xomw_param_map)mImageParams.Get_by(handlerClass);
		// NOTE: lazy-init; code below can be inefficent
		if (rv.paramMap == null) {
			// Initialise static lists				
			if (internalParamNames == null) {
				internalParamNames = new Xomw_param_list[]
				{ Xomw_param_list.New(Xomw_param_map.Type__horizAlign, "horizAlign", "left", "right", "center", "none")
				, Xomw_param_list.New(Xomw_param_map.Type__vertAlign , "vertAlign", "baseline", "sub", "super", "top", "text-top", "middle", "bottom", "text-bottom")
				, Xomw_param_list.New(Xomw_param_map.Type__frame     , "frame", "thumbnail", "manual_thumb", "framed", "frameless", "upright", "border", "link", "alt", "class")
				};

				internalParamMap = new Xomw_param_map();
				byte[] bry_img = Bry_.new_a7("img_");
				for (Xomw_param_list param_list : internalParamNames) {
					for (byte[] name : param_list.names) {
						byte[] magic_name = Bry_.Add(bry_img, Bry_.Replace(name, Byte_ascii.Dash, Byte_ascii.Underline));
						internalParamMap.Add(magic_name, param_list.type_uid, name);
					}
				}
			}

			// Add handler params
			Xomw_param_map paramMap = internalParamMap.Clone();
			if (handler != null) {
				Xomw_param_map handlerParamMap = handler.getParamMap();
				int handlerParamMapLen = handlerParamMap.Len();
				for (int i = 0; i < handlerParamMapLen; i++) {
					Xomw_param_itm itm = (Xomw_param_itm)handlerParamMap.Get_at(i);
					paramMap.Add(itm.magic, itm.type_uid, itm.name);
				}
			}
			this.mImageParams.Add(handlerClass, paramMap);
			rv.paramMap = paramMap;
			Xomw_MagicWordArray mw_array = new Xomw_MagicWordArray(env.Magic_word_mgr(), paramMap.Keys());
			this.mImageParamsMagicArray.Add(handlerClass, mw_array);
			rv.mwArray = mw_array;
		}
		else {
			rv.mwArray = (Xomw_MagicWordArray)mImageParamsMagicArray.Get_by(handlerClass);
		}
	}
	// Parsed a width param of imagelink like 300px or 200x300px
	// XO.MW.NOTE: for MW, "" -> null, null while "AxB" -> 0x0
	public void parseWidthParam(int[] img_size, byte[] src) {
		img_size[0] = img_size[1] = -1;
		if (src == Bry_.Empty) {
			return;
		}
		// (T15500) In both cases (width/height and width only),
		// permit trailing "px" for backward compatibility.
		int src_bgn = 0;
		int src_end = src.length;
		// XO: "px" is optional; if exists at end, ignore it
		if (Bry_.Has_at_end(src, Bry__px)) {
			src_end -= 2;
		}

		// XO.MW: if ( preg_match( '/^([0-9]*)x([0-9]*)\s*(?:px)?\s*$/', $value, $m ) ) {
		int w_bgn = 0;
		int w_end = Bry_find_.Find_fwd_while_num(src, src_bgn, src_end);
		int h_bgn = -1;
		int h_end = -1;
		if (w_end < src_end && src[w_end] == Byte_ascii.Ltr_x) {
			h_bgn = w_end + 1;
			h_end = Bry_find_.Find_fwd_while_num(src, h_bgn, src_end);
		}
		img_size[0] = Bry_.To_int_or(src, w_bgn, w_end, 0);
		img_size[1] = Bry_.To_int_or(src, h_bgn, h_end, 0);
	}
	private static final    byte[] Bry__px = Bry_.new_a7("px");

	/**
	* Fetch a file and its title and register a reference to it.
	* If 'broken' is a key in $options then the file will appear as a broken thumbnail.
	* @param Title $title
	* @param array $options Array of options to RepoGroup::findFile
	* @return array ( File or false, Title of file )
	*/
	public Xomw_File fetchFileAndTitle(Xoa_ttl title, Hash_adp options) {
		Xomw_File file = fetchFileNoRegister(title, options);

		//$time = $file ? $file->getTimestamp() : false;
		//$sha1 = $file ? $file->getSha1() : false;
		//# Register the file as a dependency...
		//$this->mOutput->addImage( $title->getDBkey(), $time, $sha1 );
		//if ( $file && !$title->equals( $file->getTitle() ) ) {
		//	# Update fetched file title
		//	$title = $file->getTitle();
		//	$this->mOutput->addImage( $title->getDBkey(), $time, $sha1 );
		//}
		return file;
	}
	/**
	* Helper function for fetchFileAndTitle.
	*
	* Also useful if you need to fetch a file but not use it yet,
	* for example to get the file's handler.
	*
	* @param Title $title
	* @param array $options Array of options to RepoGroup::findFile
	* @return File|boolean
	*/
	private Xomw_File fetchFileNoRegister(Xoa_ttl title, Hash_adp options) {
		Xomw_File file = null;
//			if ( isset( $options['broken'] ) ) {
//				file = false; // broken thumbnail forced by hook
//			} elseif ( isset( $options['sha1'] ) ) { // get by (sha1,timestamp)
//				file = RepoGroup::singleton()->findFileFromKey( $options['sha1'], $options );
//			} else { // get by (name,timestamp)
			file = env.File_finder().Find_file(title);	// $options 
//			}
		return file;
	}
	public void Maybe_do_subpage_link(Xomw_linker__normalize_subpage_link rv, byte[] target, byte[] text) {
		linker.Normalize_subpage_link(rv, page_title, target, text);
	}
	public void Replace_link_holders(Xomw_parser_ctx pctx, Xomw_parser_bfr pbfr) {
		holders.Replace(pctx, pbfr);
	}
	public void Make_known_link_holder(Bry_bfr bfr, Xoa_ttl nt, byte[] text, byte[] trail, byte[] prefix) {
		byte[][] split_trail = linker.Split_trail(trail);
		byte[] inside = split_trail[0];
		trail = split_trail[1];

		if (text == Bry_.Empty) {
			text = Bry_.Escape_html(nt.Get_prefixed_text()); 
		}

		// PORTED:new HtmlArmor( "$prefix$text$inside" )
		tmp.Add_bry_escape_html(prefix);
		tmp.Add_bry_escape_html(text);
		tmp.Add_bry_escape_html(inside);
		text = tmp.To_bry_and_clear();
		
		link_renderer.Make_known_link(bfr, nt, text, extra_atrs, query);
		byte[] link = bfr.To_bry_and_clear();
		parser.Armor_links(bfr, link, 0, link.length);
		bfr.Add(trail);
	}

	private static boolean[] title_chars_for_lnki;
	private static final    byte[] Bry__wtxt__lnki__bgn = Bry_.new_a7("[["), Bry__wtxt__lnki__end = Bry_.new_a7("]]");

	// $e1 = "/^([{$tc}]+)(?:\\|(.+?))?]](.*)\$/sD";
	// 
	// REGEX: "title-char"(1+) + "pipe"(0-1) + "]]"(0-1) + "other chars up to next [["
	//   title-char             -> ([{$tc}]+)
	//   pipe                   -> (?:\\|(.+?))?
	//   ]]                     -> ?]]
	//   other chars...         -> (.*)

	// $e1_img = "/^([{$tc}]+)\\|(.*)\$/sD";
	// 
	// REGEX: "title-char"(1+) + "pipe"(0-1) + "other chars up to next [["
	//   title-char             -> ([{$tc}]+)
	//   pipe                   -> \\|
	//   other chars...         -> (.*)
}
