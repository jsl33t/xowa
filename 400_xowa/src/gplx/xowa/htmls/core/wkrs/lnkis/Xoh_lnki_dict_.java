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
package gplx.xowa.htmls.core.wkrs.lnkis; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*; import gplx.xowa.htmls.core.wkrs.*;
import gplx.core.brys.*;
public class Xoh_lnki_dict_ {
	public static final byte	// SERIALIZED
	  Capt__same					= Xoh_ttl_matcher.Tid__same		// EX: [[A]]
	, Capt__diff					= Xoh_ttl_matcher.Tid__diff		// EX: [[A|b]]
	, Capt__trail					= Xoh_ttl_matcher.Tid__tail		// EX: [[A|b]]
	, Capt__head					= Xoh_ttl_matcher.Tid__head		// EX: [[A_(b)|A]]
	;
	public static void Ns_encode(Bry_bfr bfr, int ns_id) {
		gplx.xowa.htmls.core.hzips.Xoh_hzip_int_.Encode(1, bfr, ns_id + 2);
	}
	public static int Ns_decode(Bry_rdr rdr) {
		return rdr.Read_int_by_base85(1) - 2;
	}
}
