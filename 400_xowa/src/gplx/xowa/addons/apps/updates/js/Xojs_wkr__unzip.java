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
package gplx.xowa.addons.apps.updates.js; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.apps.*; import gplx.xowa.addons.apps.updates.*;
import gplx.xowa.guis.cbks.*;	
import gplx.core.ios.zips.*;
public class Xojs_wkr__unzip extends Xojs_wkr__base {
	private final    Io_url src, trg;
	public Xojs_wkr__unzip(Xog_cbk_mgr cbk_mgr, Xog_cbk_trg cbk_trg, String js_cbk, Gfo_invk_cmd done_cbk, Io_url src, Io_url trg, long prog_data_end) {super(cbk_mgr, cbk_trg, js_cbk, done_cbk);
		this.src = src; this.trg = trg;
		this.Prog_data_end_(prog_data_end);
	}
	@Override protected void Exec_run() {
		Io_zip_decompress_cmd decompress = Io_zip_decompress_cmd_.Proto.Make_new();
		List_adp unzip_urls = List_adp_.New();
		decompress.Exec(this, src, trg, unzip_urls);
	}
}