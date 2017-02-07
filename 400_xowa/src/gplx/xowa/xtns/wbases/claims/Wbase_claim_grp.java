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
package gplx.xowa.xtns.wbases.claims; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wbases.*;
import gplx.core.primitives.*;
import gplx.xowa.xtns.wbases.claims.itms.*;
public class Wbase_claim_grp {
	public Wbase_claim_grp(Int_obj_ref id_ref, Wbase_claim_base[] itms) {this.id_ref = id_ref; this.itms = itms;}
	public Int_obj_ref Id_ref() {return id_ref;} private final    Int_obj_ref id_ref;
	public int Id() {return id_ref.Val();}
	public String Id_str() {if (id_str == null) id_str = "P" + Int_.To_str(id_ref.Val()); return id_str;} private String id_str;
	public int Len() {return itms.length;} private Wbase_claim_base[] itms;
	public Wbase_claim_base Get_at(int i) {return itms[i];}
	public static List_adp Xto_list(Ordered_hash hash) {
		int len = hash.Count();
		List_adp rv = List_adp_.New();
		for (int i = 0; i < len; ++i) {
			Wbase_claim_grp grp = (Wbase_claim_grp)hash.Get_at(i);
			int grp_len = grp.Len();
			for (int j = 0; j < grp_len; ++j)
				rv.Add(grp.Get_at(j));
		}
		return rv;
	}
}
