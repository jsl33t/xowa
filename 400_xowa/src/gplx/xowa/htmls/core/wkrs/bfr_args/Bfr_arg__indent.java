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
package gplx.xowa.htmls.core.wkrs.bfr_args; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*; import gplx.xowa.htmls.core.wkrs.*;
public class Bfr_arg__indent extends gplx.core.brys.Bfr_arg_base implements gplx.core.brys.Clear_able {
	private int indent = 0;
	public void Clear() {
		this.indent = 0;
	}
	public void Set(int v) {this.indent = v;}
	@Override public void Bfr_arg__add(Bry_bfr bfr) {
		if (indent > 0)
			bfr.Add_byte_repeat(Byte_ascii.Space, indent * 2);
	}
}
