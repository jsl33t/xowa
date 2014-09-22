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
package gplx.xowa.hdumps; import gplx.*; import gplx.xowa.*;
import gplx.xowa.wikis.xwikis.*; import gplx.xowa.langs.cases.*;
public class Xowv_wiki {
	private Xoav_app app;
	public Xowv_wiki(Xoav_app app, String domain_str, Io_url wiki_root_dir) {
		this.app = app;
		this.domain_str = domain_str; this.domain_bry = Bry_.new_utf8_(domain_str);
		this.ns_mgr = Xow_ns_mgr_.default_(app.Utl_case_mgr()); // new Xow_ns_mgr(app.Utl_case_mgr()); // FIXME
		this.db_mgr = new Xowv_db_mgr(domain_str, wiki_root_dir);
		this.hdump_mgr = new Xowd_hdump_mgr(app, this);
		this.xwiki_mgr = new Xow_xwiki_mgr();
	}
	public byte[] Domain_bry() {return domain_bry;} private final byte[] domain_bry;
	public String Domain_str() {return domain_str;} private final String domain_str;
	public Xow_ns_mgr Ns_mgr() {return ns_mgr;} private final Xow_ns_mgr ns_mgr;
	public Xowv_db_mgr Db_mgr() {return db_mgr;} private final Xowv_db_mgr db_mgr;
	public Xowd_hdump_mgr Hdump_mgr() {return hdump_mgr;} private final Xowd_hdump_mgr hdump_mgr;
	public Xow_xwiki_mgr Xwiki_mgr() {return xwiki_mgr;} private Xow_xwiki_mgr xwiki_mgr;
	public Xoa_ttl Ttl_parse(byte[] ttl) {
		return Xoa_ttl.parse(app.Utl_bfr_mkr(), app.Utl_amp_mgr(), app.Utl_case_mgr(), xwiki_mgr, ns_mgr, app.Utl_msg_log(), ttl, 0, ttl.length);
	}
}