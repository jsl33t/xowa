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
package gplx.xowa.htmls.core.wkrs.hdrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*; import gplx.xowa.htmls.core.wkrs.*;
import gplx.langs.htmls.*; import gplx.langs.htmls.parsers.*;
import gplx.xowa.htmls.sections.*;
public class Xoh_hdr_make {
	public void Make(Bry_bfr bfr, Xoh_page hpg, byte[] src, Xoh_hdr_parser arg) {
		// , int rng_bgn, int rng_end, int level, int capt_bgn, int capt_end, byte[] anch
		// register section
		int rng_bgn = arg.Rng_bgn(), rng_end = arg.Rng_end();
		int level = arg.Hdr_level();
		Xoh_section_mgr section_mgr = hpg.Section_mgr();
		int section_len = section_mgr.Len();
		if (section_len != 0)	// guard against -1 index; should not happen
			section_mgr.Set_content(section_len - 1, src, rng_bgn - 2);	// -2 to skip "\n\n"
		byte[] capt = Bry_.Mid(src, arg.Capt_bgn(), arg.Capt_end());
		byte[] anch = arg.Anch_bry();
		if (anch == null) anch = Bry_.Replace(capt, Byte_ascii.Space, Byte_ascii.Underline);
		hpg.Section_mgr().Add(section_len, level, anch, capt).Content_bgn_(rng_end + 1); // +1 to skip "\n"
		bfr.Add_mid(src, rng_bgn, rng_end);
	}
}