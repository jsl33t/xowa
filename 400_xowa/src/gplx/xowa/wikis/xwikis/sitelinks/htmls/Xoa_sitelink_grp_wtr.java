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
class Xoa_sitelink_grp_wtr implements gplx.core.brys.Bfr_arg {
	private final    Xoa_sitelink_itm_wtr itm_wtr = new Xoa_sitelink_itm_wtr();
	private Xoa_sitelink_grp_mgr mgr; 
	public void Init_by_app(Xoa_app app) {itm_wtr.Init_by_app(app);}
	public Xoa_sitelink_grp_wtr Fmt__init(Xoa_sitelink_grp_mgr mgr) {this.mgr = mgr; return this;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		int len = mgr.Len();
		for (int i = 0; i < len; ++i) {
			Xoa_sitelink_grp grp = mgr.Get_at(i);
			if (grp.Active_len() == 0) continue;	// skip grps with no items
			fmtr.Bld_bfr_many(bfr, grp.Name(), itm_wtr.Fmt__init(grp));
		}
	}
	private static final    Bry_fmtr fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "  <h4>~{all_name}</h4>"
	, "  <table style='width: 100%;'>~{grps}"
	, "  </table>"
	), "all_name", "grps");
}
