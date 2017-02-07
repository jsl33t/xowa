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
package gplx.xowa.addons.htmls.tocs; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.htmls.*;
public class Xoh_toc_itm {// EX: <li class="toclevel-3 tocsection-3"><a href="#aaa"><span class="tocnumber">1.1.1</span> <span class="toctext">aaa</span></a></li>
	public int		Uid() {return uid;} private int uid;		// uid of itm; HTML: "tocsection-3"
	public int		Lvl() {return lvl;} private int lvl;		// indent level; HTML: "toclevel-3"
	public int[]	Path() {return path;} private int[] path;	// path of itm; HTML: "1.1.1"
	public byte[]	Anch() {return anch;} private byte[] anch;	// HTML: "#aaa"
	public byte[]	Text() {return text;} private byte[] text;	// HTML: "aaa"
	public byte[]	Path_to_bry(Bry_bfr bfr) {
		int len = path.length;
		for (int i = 0; i < len; ++i) {
			if (i != 0) bfr.Add_byte_dot();
			bfr.Add_int_variable(path[i]);
		}
		return bfr.To_bry_and_clear();
	}
	public void Set__lvl(int uid, int lvl, int[] path) {this.uid = uid; this.lvl = lvl; this.path = path;}
	public Xoh_toc_itm Set__txt(byte[] anch, byte[] text) {this.anch = anch; this.text = text; return this;}
}
