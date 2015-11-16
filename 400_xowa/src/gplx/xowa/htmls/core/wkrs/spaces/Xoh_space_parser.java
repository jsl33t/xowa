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
package gplx.xowa.htmls.core.wkrs.spaces; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*; import gplx.xowa.htmls.core.wkrs.*;
import gplx.langs.htmls.*; import gplx.langs.htmls.parsers.*;
import gplx.xowa.htmls.core.hzips.*;
public class Xoh_space_parser implements Html_doc_wkr {
	private final Xoh_hdoc_wkr wkr;
	public Xoh_space_parser(Xoh_hdoc_wkr wkr) {this.wkr = wkr;}
	public byte[] Hook() {return Hook_bry;}
	public int Parse(byte[] src, int src_bgn, int src_end, int pos) {
		int rng_end = Bry_find_.Find_fwd_while(src, pos + Hook_len, src_end, Byte_ascii.Space);
		wkr.On_space(pos, rng_end);
		return rng_end;
	}
	private static final byte[] Hook_bry = Bry_.new_a7("    ");
	private static final int Hook_len = Hook_bry.length;
}
