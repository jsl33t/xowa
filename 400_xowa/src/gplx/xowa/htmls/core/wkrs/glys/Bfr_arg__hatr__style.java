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
package gplx.xowa.htmls.core.wkrs.glys; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*; import gplx.xowa.htmls.core.wkrs.*;
import gplx.xowa.htmls.core.wkrs.bfr_args.*;
class Bfr_arg__hatr__style implements gplx.core.brys.Bfr_arg {
	private final    byte[] atr_bgn;
	private int max_w, w;
	private byte[] xtra_cls;
	public Bfr_arg__hatr__style(byte[] key) {
		this.atr_bgn = Bfr_arg__hatr_.Bld_atr_bgn(key);
		this.Clear();
	}
	public void Set_args(int max_w, int w, byte[] xtra_cls) {this.max_w = max_w; this.w = w; this.xtra_cls = xtra_cls;}
	public void Clear() {max_w = 0; w = 0; xtra_cls = null;}
	public void Bfr_arg__clear() {this.Clear();}
	public boolean Bfr_arg__missing() {return max_w == 0 && xtra_cls == null;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		if (Bfr_arg__missing()) return;
		bfr.Add(atr_bgn);
		if (max_w > 0) {
			bfr.Add(Style__frag_1);
			bfr.Add_int_variable(max_w);
			bfr.Add(Style__frag_3);
		}
		if (w > 0) {
			bfr.Add_byte_space();
			bfr.Add(Style__frag_2);
			bfr.Add_int_variable(w);
			bfr.Add(Style__frag_3);
		}
		if (xtra_cls != null) {
			bfr.Add(xtra_cls);
		}
		bfr.Add_byte_quote();
	}
	private static final    byte[]
	  Style__frag_1 = Bry_.new_a7("max-width:")
	, Style__frag_2 = Bry_.new_a7("_width:")
	, Style__frag_3 = Bry_.new_a7("px;")
	;
}