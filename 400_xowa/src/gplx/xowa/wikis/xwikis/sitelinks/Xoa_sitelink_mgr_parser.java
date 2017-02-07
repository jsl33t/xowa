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
import gplx.langs.dsvs.*;
class Xoa_sitelink_mgr_parser extends Dsv_wkr_base {
	private final Xoa_sitelink_grp_mgr grp_mgr;
	private final Xoa_sitelink_itm_mgr itm_mgr;
	private int cur_tid = -1;
	private byte[] cur_fld1, cur_fld2;
	private Xoa_sitelink_grp cur_grp;
	public Xoa_sitelink_mgr_parser(Xoa_sitelink_mgr mgr) {this.grp_mgr = mgr.Grp_mgr(); this.itm_mgr = mgr.Itm_mgr();}
	@Override public Dsv_fld_parser[] Fld_parsers() {return new Dsv_fld_parser[] {Dsv_fld_parser_.Bry_parser, Dsv_fld_parser_.Bry_parser, Dsv_fld_parser_.Bry_parser};}
	@Override public boolean Write_bry(Dsv_tbl_parser parser, int fld_idx, byte[] src, int bgn, int end) {
		switch (fld_idx) {
			case 0: cur_tid		= Bry_.To_int_or(src, bgn, end, -1); return true;
			case 1: cur_fld1	= Bry_.Mid(src, bgn, end); return true;
			case 2: cur_fld2	= Bry_.Mid(src, bgn, end); return true;
			default: return false;
		}
	}
	@Override public void Commit_itm(Dsv_tbl_parser parser, int pos) {
		switch (cur_tid) {
			case Tid__grp:
				cur_grp = grp_mgr.Get_by_or_new(cur_fld1);
				break;
			case Tid__itm:
				Xoa_sitelink_itm itm = itm_mgr.Get_by_or_new(cur_fld1);
				itm.Move_to(cur_grp);
				itm.Site_name_(cur_fld2);
				break;
			default:		throw Err_.new_unhandled(cur_tid);
		}
		cur_tid = -1;
		cur_fld1 = cur_fld2 = null;
	}
	public static final int Tid__grp = 0, Tid__itm = 1;
}
