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
package gplx.xowa.addons.wikis.pages.syncs.core.parsers; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.wikis.*; import gplx.xowa.addons.wikis.pages.*; import gplx.xowa.addons.wikis.pages.syncs.*; import gplx.xowa.addons.wikis.pages.syncs.core.*;
import gplx.core.ios.streams.*;
import gplx.xowa.htmls.core.dbs.*; import gplx.xowa.htmls.core.hzips.*;
import gplx.xowa.wikis.data.*; import gplx.xowa.wikis.data.tbls.*; import gplx.xowa.wikis.pages.*;
public class Xowd_html_tbl_mgr {
	public Xow_db_file Get_html_db(Xow_wiki wiki) {
		return wiki.Data__core_mgr().Dbs__assert_by_tid(Xow_db_file_.Tid__html_user);
	}
	public void Save_html(Xow_wiki wiki, Xow_db_file db, int page_id, int revn_id, byte[] src) {
		Xowd_html_tbl tbl = new Xowd_html_tbl(db.Conn());

		// set other html props to null; TODO_FUTURE:need to parse html to get these
		byte[] display_ttl = null;
		byte[] content_sub = null;
		byte[] sidebar_div = null;
		db.Conn().Meta_tbl_assert(tbl);
		tbl.Upsert(page_id, Xopg_module_mgr.Tid_null, Io_stream_tid_.Tid__raw, Xoh_hzip_dict_.Hzip__plain, display_ttl, content_sub, sidebar_div, src);

		wiki.Data__core_mgr().Db__core().Tbl__page().Update__html_db_id(page_id, db.Id());
	}
}
