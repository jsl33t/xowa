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
package gplx.xowa.parsers.tmpls; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import gplx.core.btries.*; import gplx.html.*;
public class Nowiki_escape_itm {
	public Nowiki_escape_itm(byte[] src, byte[] trg) {this.src = src; this.trg = trg; this.src_adj = src.length - 1;}
	private int src_adj;
	public byte[] Src() {return src;} private byte[] src;
	public byte[] Trg() {return trg;} private byte[] trg;
	public static boolean Escape(Bry_bfr tmp_bfr, byte[] src, int bgn, int end) {// <nowiki> works by escaping all wtxt symbols so that wtxt parser does not hook into any of them
		boolean dirty = false;
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			Object o = trie.Match_bgn_w_byte(b, src, i, end);
			if (o == null) {
				if (dirty)
					tmp_bfr.Add_byte(b);
			}
			else {
				if (!dirty) {
					tmp_bfr.Add_mid(src, bgn, i);
					dirty = true;
				}
				Nowiki_escape_itm itm = (Nowiki_escape_itm)o;
				tmp_bfr.Add(itm.Trg());
				i += itm.src_adj;
			}
		}
		return dirty;
	}
	private static final byte[] Pre_bry = new byte[] {Byte_ascii.NewLine, Byte_ascii.Space};	// NOTE: must go before trie_new
	private static final Btrie_slim_mgr trie = trie_new();
	private static Btrie_slim_mgr trie_new() {
		Btrie_slim_mgr rv = Btrie_slim_mgr.cs_();
		trie_new_itm(rv, Byte_ascii.Lt_bry				, Html_entity_.Lt_bry);
		trie_new_itm(rv, Byte_ascii.Brack_bgn_bry		, Html_entity_.Brack_bgn_bry);
		trie_new_itm(rv, Byte_ascii.Brack_end_bry		, Html_entity_.Brack_end_bry);	// PAGE:en.w: Tall_poppy_syndrome DATE:2014-07-23
		trie_new_itm(rv, Byte_ascii.Pipe_bry			, Html_entity_.Pipe_bry);
		trie_new_itm(rv, Byte_ascii.Apos_bry			, Html_entity_.Apos_key_bry);	// NOTE: for backward compatibility, use &apos; note that amp_wkr will turn &apos; -> &#39 but &#39 -> '; DATE:2014-07-03
		trie_new_itm(rv, Byte_ascii.Colon_bry			, Html_entity_.Colon_bry);
		trie_new_itm(rv, Byte_ascii.Underline_bry		, Html_entity_.Underline_bry);
		trie_new_itm(rv, Byte_ascii.Asterisk_bry		, Html_entity_.Asterisk_bry);
		trie_new_itm(rv, Byte_ascii.Space_bry			, Html_entity_.Space_bry);
		trie_new_itm(rv, Byte_ascii.NewLine_bry			, Html_entity_.Nl_bry);
		trie_new_itm(rv, Pre_bry						, Pre_bry);
		return rv;
	}
	private static void trie_new_itm(Btrie_slim_mgr rv, byte[] src, byte[] trg) {
		Nowiki_escape_itm itm = new Nowiki_escape_itm(src, trg);
		rv.Add_obj(src, itm);			
	}
}
