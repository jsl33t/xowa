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
package gplx.xowa.xtns.scribunto.libs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*;
import org.junit.*; import gplx.xowa.xtns.scribunto.engines.mocks.*;
public class Scrib_lib_ustring__match__tst {
	private final Mock_scrib_fxt fxt = new Mock_scrib_fxt(); private Scrib_lib lib;
	@Before public void init() {
		fxt.Clear();
		lib = fxt.Core().Lib_ustring().Init();
	}
	@Test  public void Basic() {
		Exec_match("abcd"	, "bc"				, 1, "bc");							// basic
		Exec_match("abcd"	, "x"				, 1, String_.Null_mark);			// empty
		Exec_match("abcd"	, "a"				, 2, String_.Null_mark);			// bgn
		Exec_match("abcd"	, "b(c)"			, 1, "c");							// group
		Exec_match(" a b "	, "^%s*(.-)%s*$"	, 1, "a b;");						// trim; NOTE: changed from "a b" to "a b;"; DATE:2015-01-30
		Exec_match("abcd"	, "a"				, 0, "a");							// handle 0; note that php/lua is super-1, but some modules pass in 0; ru.w:Module:Infocards; DATE:2013-11-08
		Exec_match("abcd"	, "."				, -1, "d");							// -1
		Exec_match("aaa"	, "a"				, 1, "a");							// should return 1st match not many
		Exec_match("aaa"	, "(a)"				, 1, "a;a;a");						// should return all matches
		Exec_match("a b"	, "%S"				, 1, "a");							// %S was returning every match instead of 1st; PAGE:en.w:Bertrand_Russell; DATE:2014-04-02
		Exec_match(1		, "a"				, 1, String_.Null_mark);			// Module can pass raw ints; PAGE:en.w:Budget_of_the_European_Union; DATE:2015-01-22
		Exec_match(""		, "a?"				, 1, "");							// no results with ? should return "" not nil; PAGE:en.d:民; DATE:2015-01-30
	}
	@Test  public void Args_out_of_order() {
		fxt.Test__proc__kvps__empty(lib, Scrib_lib_ustring.Invk_match, Keyval_.Ary(Keyval_.int_(2, "[a]")));
	}
//		@Test  public void Match_viwiktionary() {
//			fxt.Init_cbk(Scrib_core.Key_mw_interface, fxt.Core().Lib_ustring(), Scrib_lib_ustring.Invk_match);
//			Exec_match("tr"	, "()(r)", 1, ";");						// should return all matches
//			Exec_match("tr"	, "^([b]*).-([c]*)$", 1, ";");						// should return all matches
//		}
	private void Exec_match(Object text, String regx, int bgn, String expd) {
		fxt.Test__proc__kvps__flat(lib, Scrib_lib_ustring.Invk_match, Scrib_kv_utl_.base1_many_(text, regx, bgn), expd);
	}
}
