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
package gplx.xowa.addons.bldrs.centrals.cmds; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.bldrs.*; import gplx.xowa.addons.bldrs.centrals.*;
import gplx.core.security.*;
import gplx.xowa.wikis.*;
public class Xobc_cmd__wiki_reg extends Xobc_cmd__base {
	private final    Io_url wiki_dir;
	private final    String wiki_domain;
	public Xobc_cmd__wiki_reg(Xobc_task_mgr task_mgr, int task_id, int step_id, int cmd_idx, Io_url wiki_dir, String wiki_domain) {super(task_mgr, task_id, step_id, cmd_idx);
		this.wiki_dir = wiki_dir;
		this.wiki_domain = wiki_domain;
	}
	@Override public String Cmd_type() {return CMD_TYPE;} public static final    String CMD_TYPE = "xowa.wiki.reg";
	@Override public String Cmd_name() {return "import";}		
	@Override protected void Cmd_exec_hook(Xobc_cmd_ctx ctx) {
		ctx.App().User().User_db_mgr().Init_site_mgr();	// must init for wiki.register cmd

		// get wiki_core_url
		Io_url[] wiki_fils = Io_mgr.Instance.QueryDir_fils(wiki_dir);
		Io_url wiki_core_url = null;
		int len = wiki_fils.length;
		for (int i = 0; i < len; ++i) {
			Io_url url = wiki_fils[i];
			if (gplx.xowa.wikis.data.Xow_db_file__core_.Is_core_fil_name(wiki_domain, url.NameAndExt())) {
				wiki_core_url = url;
				break;
			}
		}
		if (wiki_core_url == null) throw Err_.new_("wiki_import", "import_url not found", "domain", wiki_domain);

		// import; open
		Xoa_wiki_mgr wiki_mgr = ctx.App().Wiki_mgri();
		Gfo_invk_.Invk_by_val(wiki_mgr, gplx.xowa.wikis.Xoa_wiki_mgr_.Invk__import_by_url, wiki_core_url);
		// COMMENTED: do not auto-open wiki; wait for true-pack mode
		// Xow_wiki wiki = wiki_mgr.Get_by_or_null(Bry_.new_u8(wiki_domain));
		// Gfo_invk_.Invk_by_msg(ctx.App().Gui__tab_mgr()	, gplx.xowa.guis.tabs.Xog_tab_mgr_.Invk__new_tab, GfoMsg_.new_cast_("").Add("focus", true).Add("site", wiki_domain).Add("page", String_.new_u8(wiki.Props().Main_page())));
	}
}
