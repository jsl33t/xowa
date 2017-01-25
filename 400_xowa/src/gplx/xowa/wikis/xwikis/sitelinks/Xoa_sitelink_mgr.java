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
package gplx.xowa.wikis.xwikis.sitelinks; import gplx.*; import gplx.xowa.*; import gplx.xowa.wikis.*; import gplx.xowa.wikis.xwikis.*;
import gplx.xowa.wikis.xwikis.sitelinks.htmls.*; import gplx.xowa.langs.*;
public class Xoa_sitelink_mgr {
	private final    Xoa_sitelink_div_wtr div_wtr = new Xoa_sitelink_div_wtr();
	public Xoa_sitelink_itm_mgr Itm_mgr() {return itm_mgr;} private final    Xoa_sitelink_itm_mgr itm_mgr;
	public Xoa_sitelink_grp_mgr Grp_mgr() {return grp_mgr;} private final    Xoa_sitelink_grp_mgr grp_mgr = new Xoa_sitelink_grp_mgr();
	public Xoa_sitelink_mgr() {
		this.itm_mgr = new Xoa_sitelink_itm_mgr(grp_mgr.Default_grp());
	}
	public void Init_by_app() { // add all langs
		Xoa_sitelink_grp default_grp = grp_mgr.Default_grp();
		Xol_lang_stub[] ary = Xol_lang_stub_.Ary();
		int len = ary.length;
		for (int i = 0; i < len; ++i) {
			Xol_lang_stub stub = ary[i];
			Xoa_sitelink_itm itm = itm_mgr.Get_by(stub.Key());
			if (itm == null) {	// note that some itms may already exist if user has defined custom names
				itm = new Xoa_sitelink_itm(default_grp, stub.Key(), stub.Canonical_name());
				itm_mgr.Add(itm);
			}
		}
	}
	public void Parse(byte[] src) {
		Xoa_sitelink_mgr_parser parser = new Xoa_sitelink_mgr_parser(this);
		parser.Load_by_bry(src);
		grp_mgr.Sort();	// sort again to put "Others" at bottom
	}
	public void Write_html(Bry_bfr bfr, Xowe_wiki wiki, List_adp slink_list, byte[] qid) {
		div_wtr.Write(bfr, wiki, this, slink_list, qid);
	}
}