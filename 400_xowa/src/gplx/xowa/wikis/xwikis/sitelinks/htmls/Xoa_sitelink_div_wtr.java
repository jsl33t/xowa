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
package gplx.xowa.wikis.xwikis.sitelinks.htmls; import gplx.*; import gplx.xowa.*; import gplx.xowa.wikis.*; import gplx.xowa.wikis.xwikis.*; import gplx.xowa.wikis.xwikis.sitelinks.*;
import gplx.core.brys.fmtrs.*;
import gplx.xowa.apps.apis.xowa.html.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.msgs.*;
import gplx.xowa.xtns.wdatas.core.*;
public class Xoa_sitelink_div_wtr {
	private final Xoa_sitelink_grp_wtr grp_wtr = new Xoa_sitelink_grp_wtr();
	public void Write(Bry_bfr bfr, Xowe_wiki wiki, Xoa_sitelink_mgr mgr, List_adp slink_list, byte[] qid) {
		Xoa_sitelink_grp_mgr grp_mgr = mgr.Grp_mgr(); Xoa_sitelink_itm_mgr itm_mgr = mgr.Itm_mgr();

		// reset grps
		grp_wtr.Init_by_app(wiki.App());
		int grp_len = grp_mgr.Len();
		for (int i = 0; i < grp_len; ++i) {
			Xoa_sitelink_grp grp = grp_mgr.Get_at(i);
			grp.Reset();
		}

		// add itms to each grp
		int slink_len = slink_list.Count();
		for (int i = 0; i < slink_len; i++) {
			Wdata_sitelink_itm slink = (Wdata_sitelink_itm)slink_list.Get_at(i);
			Xoa_ttl ttl = slink.Page_ttl();
			Xoa_sitelink_itm itm = itm_mgr.Get_by(ttl.Wik_itm().Key_bry());
			if (itm == null) {
				Xoa_app_.Usr_dlg().Warn_many("", "", "sitelink itm missing; wiki=~{0} key=~{1} lnki_ttl=~{2}", wiki.Domain_bry(), ttl.Wik_itm().Key_bry(), ttl.Raw());
				continue;
			}
			byte[] ttl_bry = ttl.Page_txt_w_anchor();
			boolean ttl_is_empty = false;
			if (Bry_.Len_eq_0(ttl_bry)) {	// NOTE: handles ttls like [[fr:]] and [[:fr;]] which have an empty Page_txt, but a valued Full_txt_raw
				ttl_bry = wiki.Parser_mgr().Ctx().Cur_page().Ttl().Page_txt();
				ttl_is_empty = true;
			}
			itm.Init_by_page(slink.Domain_info(), ttl_bry, ttl_is_empty, slink.Badges());				
		}

		// write html
		Xoapi_toggle_itm toggle_itm = wiki.Appe().Api_root().Html().Page().Toggle_mgr().Get_or_new("wikidata-langs");
		toggle_itm.Init(wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_page_lang_header));
		Bry_bfr tmp_bfr = wiki.Utl__bfr_mkr().Get_b128().Mkr_rls();
		byte[] wikidata_link = Bry_.Len_eq_0(qid) ? Bry_.Empty : wbase_fmtr.Bld_bry_many(tmp_bfr, qid);
		div_fmtr.Bld_bfr_many(bfr, slink_len, wikidata_link, toggle_itm.Html_toggle_btn(), toggle_itm.Html_toggle_hdr(), grp_wtr.Fmt__init(grp_mgr));
	}
	private static final Bry_fmtr wbase_fmtr = Bry_fmtr.new_(" (<a href=\"/site/www.wikidata.org/wiki/~{qid}\">wikidata</a>)", "qid");
	private static final Bry_fmtr div_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( "<div id=\"xowa-lang\">"
	, "  <h5>~{toggle_btn} (links: ~{len}) ~{wikidata_link}</h5>"
	, "  <div~{toggle_hdr}>~{grps}"
	, "  </div>"
	, "</div>"
	), "len", "wikidata_link", "toggle_btn", "toggle_hdr", "grps");
}
