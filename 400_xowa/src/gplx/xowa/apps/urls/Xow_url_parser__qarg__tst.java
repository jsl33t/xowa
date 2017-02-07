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
package gplx.xowa.apps.urls; import gplx.*; import gplx.xowa.*; import gplx.xowa.apps.*;
import org.junit.*;
public class Xow_url_parser__qarg__tst {
	private final    Xow_url_parser_fxt fxt = new Xow_url_parser_fxt();
	@Test  public void Redirect() {
		fxt.Exec__parse("A?redirect=no").Test__wiki("en.wikipedia.org").Test__page("A").Test__qargs("?redirect=no");
	}
	@Test  public void Action_is_edit() {
		fxt.Exec__parse("A?action=edit").Test__wiki("en.wikipedia.org").Test__page("A").Test__action_is_edit_y();
	}
	@Test  public void Assert_state_cleared() {	// PURPOSE.fix: action_is_edit (et. al.) was not being cleared on parse even though Xoa_url reused; DATE:20121231
		fxt.Exec__parse("A?action=edit")	.Test__action_is_edit_y();
		fxt.Exec__parse_reuse("B")			.Test__action_is_edit_n();
	}
	@Test  public void Query_arg() {	// PURPOSE.fix: query args were not printing out
		fxt.Exec__parse("en.wikipedia.org/wiki/Special:Search/Earth?fulltext=yes").Test__build_str_is_same();
	}
	@Test   public void Dupe_key() {
		fxt.Exec__parse("A?B=C1&B=C2").Test__page("A").Test__qargs("?B=C1&B=C2");
	}
	@Test  public void Question_is_eos() {
		fxt.Exec__parse("A?").Test__wiki("en.wikipedia.org").Test__page("A?").Test__qargs("");
	}
	@Test  public void Question_is_page() {
		fxt.Exec__parse("A?B").Test__wiki("en.wikipedia.org").Test__page("A?B").Test__qargs("");
	}
	@Test  public void Question_is_anchor() {
		fxt.Exec__parse("A#b?c").Test__wiki("en.wikipedia.org").Test__page("A").Test__anch("b.3Fc");
	}
	@Test  public void Title_remove_w() {	// PURPOSE: fix /w/ showing up as seg; DATE:2014-05-30
		fxt.Exec__parse("http://en.wikipedia.org/w/index.php?title=A").Test__wiki("en.wikipedia.org").Test__page("A");
	}
	@Test  public void Ctg() {
		fxt.Exec__parse("Category:A?pagefrom=A#mw-pages").Test__page("Category:A").Test__qargs("?pagefrom=A").Test__anch("mw-pages");
	}
	@Test  public void Anch() {
		fxt.Exec__parse("A?k1=v1#anch");
		fxt.Test__page("A");
		fxt.Test__anch("anch");
		fxt.Test__qargs("?k1=v1");
		fxt.Test__to_str("en.wikipedia.org/wiki/A?k1=v1#anch");
	}
	// DELETED: this is wrong as url should not handle html_entities like &#61; instead # should strictly designate anch_href; DATE:2016-10-10
	// @Test  public void Encoded() {
	//	fxt.Exec__parse("en.wikipedia.org/wiki/A?action&#61;edit&preload&#61;B").Test__wiki("en.wikipedia.org").Test__page("A").Test__qargs("?action=&#61;edit=&preload=&=").Test__anch("61.3BB");	// NOTE: this is wrong; fix later
	// }
}
