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
package gplx.xowa.wikis.nss; import gplx.*; import gplx.xowa.*; import gplx.xowa.wikis.*;
public class Xow_ns_ {
	public static final int	// PAGE:en.w:http://www.mediawiki.org/wiki/Help:Namespaces
	  Tid__media			=  -2
	, Tid__special			=  -1
	, Tid__main				=   0				, Tid__talk					=   1
	, Tid__user				=   2				, Tid__user_talk			=   3
	, Tid__project			=   4				, Tid__project_talk			=   5
	, Tid__file				=   6				, Tid__file_talk			=   7
	, Tid__mediawiki		=   8				, Tid__mediawiki_talk		=   9
	, Tid__template			=  10				, Tid__template_talk		=  11
	, Tid__help				=  12				, Tid__help_talk			=  13
	, Tid__category			=  14				, Tid__category_talk		=  15
	, Tid__portal			= 100				, Tid__portal_talk			= 101
	, Tid__module			= 828				, Tid__module_talk			= 829
	, Tid__null				= Int_.Min_value
	;
	public static final String
	  Key__media			= "Media"
	, Key__special			= "Special"
	, Key__main				= "(Main)"			, Key__talk					= "Talk"
	, Key__user				= "User"			, Key__user_talk			= "User talk"
	, Key__project			= "Project"			, Key__project_talk			= "Project talk"
	, Key__file				= "File"			, Key__file_talk			= "File talk"
	, Key__mediawiki		= "MediaWiki"		, Key__mediawiki_talk		= "MediaWiki talk"
	, Key__template			= "Template"		, Key__template_talk		= "Template talk"
	, Key__help				= "Help"			, Key__help_talk			= "Help talk"
	, Key__category			= "Category"		, Key__category_talk		= "Category talk"
	, Key__portal			= "Portal"			, Key__portal_talk			= "Portal talk"
	, Key__module			= "Module"			, Key__module_talk			= "Module talk"
	, Key__null				= "null"
	, Key__wikipedia		= "Wikipedia"
	;
	public static final byte[]
	  Bry__media			= Bry_.new_a7(Key__media)
	, Bry__special			= Bry_.new_a7(Key__special)
	, Bry__main				= Bry_.new_a7(Key__main)			, Bry__talk					= Bry_.new_a7(Key__talk)
	, Bry__user				= Bry_.new_a7(Key__user)			, Bry__user_talk			= Bry_.new_a7(Key__user_talk)
	, Bry__project			= Bry_.new_a7(Key__project)			, Bry__project_talk			= Bry_.new_a7(Key__project_talk)
	, Bry__file				= Bry_.new_a7(Key__file)			, Bry__file_talk			= Bry_.new_a7(Key__file_talk)
	, Bry__mediawiki		= Bry_.new_a7(Key__mediawiki)		, Bry__mediawiki_talk		= Bry_.new_a7(Key__mediawiki_talk)
	, Bry__template			= Bry_.new_a7(Key__template)		, Bry__template_talk		= Bry_.new_a7(Key__template_talk)
	, Bry__help				= Bry_.new_a7(Key__help)			, Bry__help_talk			= Bry_.new_a7(Key__help_talk)
	, Bry__category			= Bry_.new_a7(Key__category)		, Bry__category_talk		= Bry_.new_a7(Key__category_talk)
	, Bry__portal			= Bry_.new_a7(Key__portal)			, Bry__portal_talk			= Bry_.new_a7(Key__portal_talk)
	, Bry__module			= Bry_.new_a7(Key__module)			, Bry__module_talk			= Bry_.new_a7(Key__module_talk)
	, Bry__null				= Bry_.new_a7(Key__null)
	;
	public static final String
	  Alias__wikipedia = "Wikipedia"
	;
	public static final byte[] 
	  Bry__template_w_colon	= Bry_.new_a7(Key__template + ":")
	, Bry__module_w_colon	= Bry_.new_a7(Key__module + ":")
	;
}
