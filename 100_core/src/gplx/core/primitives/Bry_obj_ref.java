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
package gplx.core.primitives; import gplx.*; import gplx.core.*;
import gplx.core.brys.*;
public class Bry_obj_ref implements gplx.core.brys.Bfr_arg {
	public byte[] Val() {return val;} private byte[] val; 
	public int Val_bgn() {return val_bgn;} private int val_bgn;
	public int Val_end() {return val_end;} private int val_end;
	public Bry_obj_ref Val_(byte[] val)								{this.val = val; this.val_bgn = 0;			this.val_end = val.length;	return this;}
	public Bry_obj_ref Mid_(byte[] val, int val_bgn, int val_end)	{this.val = val; this.val_bgn = val_bgn;	this.val_end = val_end;		return this;}
	@Override public int hashCode() {return CalcHashCode(val, val_bgn, val_end);}
	@Override public boolean equals(Object obj) {
		if (obj == null) return false;	// NOTE: strange, but null check needed; throws null error; EX.WP: File:Eug�ne Delacroix - La libert� guidant le peuple.jpg
		Bry_obj_ref comp = (Bry_obj_ref)obj;
		return Bry_.Match(val, val_bgn, val_end, comp.val, comp.val_bgn, comp.val_end);	
	}	
	public void Bfr_arg__clear() {val = null;}
	public boolean Bfr_arg__exists() {return val != null && val_end > val_bgn;}
	public void Bfr_arg__add(Bry_bfr bfr) {
		if (Bfr_arg__exists())
			bfr.Add_mid(val, val_bgn, val_end);
	}
	public static int CalcHashCode(byte[] ary, int bgn, int end) {
		int rv = 0;
		for (int i = bgn; i < end; i++)
			rv = (31 * rv) + ary[i];
		return rv;
	}
	public static Bry_obj_ref New_empty()		{return New(Bry_.Empty);}
        public static Bry_obj_ref New(byte[] val)	{return new Bry_obj_ref().Val_(val);}
}
