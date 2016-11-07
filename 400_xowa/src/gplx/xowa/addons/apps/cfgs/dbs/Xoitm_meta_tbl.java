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
package gplx.xowa.addons.apps.cfgs.dbs; import gplx.*; import gplx.xowa.*; import gplx.xowa.addons.*; import gplx.xowa.addons.apps.*; import gplx.xowa.addons.apps.cfgs.*;
import gplx.dbs.*; import gplx.dbs.utls.*;
public class Xoitm_meta_tbl implements Db_tbl {
	private final    Dbmeta_fld_list flds = new Dbmeta_fld_list();
	private final    String fld__itm_id, fld__itm_key, fld__itm_scope_id, fld__itm_type_id, fld__itm_dflt;
	private final    Db_conn conn;
	public Xoitm_meta_tbl(Db_conn conn) {
		this.conn = conn;
		this.tbl_name				= "cfg_itm_meta";
		this.fld__itm_id			= flds.Add_int("itm_id");					// EX: '2'
		this.fld__itm_scope_id		= flds.Add_int("itm_scope_id");				// EX: '1'; REF: cfg_scope_regy; ENUM: app-only, wiki-only, ...
		this.fld__itm_type_id		= flds.Add_int("itm_type_id");				// EX: '1'; REF: cfg_type_regy; ENUM: int, String, ...
		this.fld__itm_key			= flds.Add_str("itm_key", 255);				// EX: 'cfg_1'
		this.fld__itm_dflt			= flds.Add_str("itm_dflt", 4096);			// EX: 'abc'
		conn.Rls_reg(this);
	}
	public String Tbl_name() {return tbl_name;} private final    String tbl_name;
	public void Create_tbl() {conn.Meta_tbl_create(Dbmeta_tbl_itm.New(tbl_name, flds));}
	public void Upsert(int itm_id, int scope_id, int type_id, String itm_key, String itm_dflt) {
		Db_tbl__crud_.Upsert(conn, tbl_name, flds, String_.Ary(fld__itm_id), itm_id, scope_id, type_id, itm_key, itm_dflt);
	}
	public Xoitm_meta_itm Select_by_key_or_null(String key) {
		Db_rdr rdr = conn.Stmt_select(tbl_name, flds, fld__itm_key).Exec_select__rls_auto();
		try {return rdr.Move_next() ? Load(rdr) : null;}
		finally {rdr.Rls();}
	}
	private Xoitm_meta_itm Load(Db_rdr rdr) {
		return new Xoitm_meta_itm
		( rdr.Read_int(fld__itm_id)
		, rdr.Read_int(fld__itm_scope_id)
		, rdr.Read_int(fld__itm_type_id)
		, rdr.Read_str(fld__itm_key)
		, rdr.Read_str(fld__itm_dflt)
		);
	}
	public void Rls() {}
}
