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
package gplx.xowa.htmls.core.hzips; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*;
import gplx.core.brys.*;
import gplx.xowa.htmls.core.wkrs.*;
public interface Xoh_hzip_wkr extends gplx.core.threads.poolables.Gfo_poolable_itm {
	String Key();
	int Decode(Bry_bfr bfr, boolean write_to_bfr, Xoh_hdoc_ctx ctx, Xoh_page hpg, Bry_rdr parser, byte[] src, int hook_bgn);
}
