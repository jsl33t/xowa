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
package gplx.xowa.htmls.bridges.dbuis.tbls; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.bridges.*; import gplx.xowa.htmls.bridges.dbuis.*;
public class Dbui_val_hash {
	private final Ordered_hash hash = Ordered_hash_.New_bry();
	public void Add(byte[] key, Dbui_val_itm itm) {hash.Add(key, itm);}
	public byte[] Get_val_as_bry(String key) {return Get_val_as_bry(Bry_.new_u8(key));}
	public byte[] Get_val_as_bry(byte[] key) {
		Dbui_val_itm itm = (Dbui_val_itm)hash.Get_by(key); if (itm == null) throw Err_.new_wo_type("dbui.val_hash; unknown key", "key", key);
		return itm.Data();
	}
}
