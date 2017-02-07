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
package gplx.xowa.xtns.math.texvcs.lxrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.math.*; import gplx.xowa.xtns.math.texvcs.*;
import gplx.xowa.xtns.math.texvcs.tkns.*;
class Texvc_lxr__leaf implements Texvc_lxr {
	public Texvc_lxr__leaf(int tkn_tid) {this.tkn_tid = tkn_tid;} private final int tkn_tid;
	public int		Tid() {return Texvc_lxr_.Tid__raw;}
	public int		Make_tkn(Texvc_ctx ctx, Texvc_root root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		root.Regy__add(tkn_tid, tkn_tid, bgn_pos, cur_pos, null);
		return cur_pos;
	}
}
