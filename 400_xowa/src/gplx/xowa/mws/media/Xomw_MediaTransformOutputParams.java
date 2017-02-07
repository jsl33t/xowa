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
package gplx.xowa.mws.media; import gplx.*; import gplx.xowa.*; import gplx.xowa.mws.*;
public class Xomw_MediaTransformOutputParams {
	public boolean desc_link;
	public byte[] alt = null;
	public byte[] title = null;
	public byte[] img_cls = null;
	public byte[] file_link = null;
	public byte[] valign = null;
	public byte[] desc_query = null;
	public byte[] override_width = null;
	public byte[] override_height = null;
	public byte[] no_dimensions = null;
	public byte[] custom_url_link = null;
	public byte[] custom_title_link = null;
	public byte[] custom_target_link = null;
	public byte[] parser_extlink_rel = null;
	public byte[] parser_extlink_target = null;
	public void Clear() {
		desc_link = false;
		alt = title = file_link = valign
			= desc_query = override_width = override_height = no_dimensions
			= custom_url_link = custom_title_link 
			= parser_extlink_rel = parser_extlink_target
			= null;
	}
}
