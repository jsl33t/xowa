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
package gplx.xowa.mws; import gplx.*; import gplx.xowa.*;
public class Xomw_MagicWordMgr {
	private final    Hash_adp_bry hash = Hash_adp_bry.cs();
	public void Add(byte[] name, boolean cs, byte[]... synonyms) {
		Xomw_MagicWord mw = new Xomw_MagicWord(name, cs, synonyms);
		hash.Add(name, mw);
	}
	public Xomw_MagicWord Get(byte[] name) {
		return (Xomw_MagicWord)hash.Get_by(name);
	}
}
