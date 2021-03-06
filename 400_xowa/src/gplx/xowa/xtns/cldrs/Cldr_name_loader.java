/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2017 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.xtns.cldrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.langs.jsons.*;
public class Cldr_name_loader {
	private final    Json_parser parser = new Json_parser();
	private final    Io_url cldr_dir;
	private final    Hash_adp files_hash = Hash_adp_.New();
	private Hash_adp urls_hash;
	private static final    String Token_cldr_names = "CldrNames", Token_json_ext = ".json";

	public Cldr_name_loader(Io_url cldr_dir) {
		this.cldr_dir = cldr_dir;
	}
	public void Clear() {
		files_hash.Clear();
		urls_hash = null;
	}
	public Cldr_name_file Load_or_empty(String lang_key) {
		// normalize to lc; scrib will pass lower_case, but underlying files are Title_case
		lang_key = String_.Lower(lang_key);

		// return file if already exists
		Cldr_name_file file = (Cldr_name_file)files_hash.Get_by(lang_key);
		if (file != null) return file;

		// create urls_hash from dir if it doesn't exist
		if (urls_hash == null)
			urls_hash = Make_urls_hash(Io_mgr.Instance.QueryDir_fils(cldr_dir));

		// get file
		Io_url url = (Io_url)urls_hash.Get_by(lang_key);
		if (url == null) {
			Gfo_usr_dlg_.Instance.Log_many("", "", "no cldrName file exists for lang; lang=~{0}", lang_key);
			return Cldr_name_file.Empty;
		}

		// load json
		byte[] json = Io_mgr.Instance.LoadFilBry(url);
		if (Bry_.Len_eq_0(json)) {
			files_hash.Add(lang_key, Cldr_name_file.Empty);
			Gfo_usr_dlg_.Instance.Warn_many("", "", "json is empty; lang=~{lang}", lang_key);
			return Cldr_name_file.Empty;
		}

		// parse, cache and return
		file = Parse(lang_key, json);
		files_hash.Add(lang_key, file);
		return file;
	}
	public Cldr_name_file Parse(String lang_key, byte[] json) { // TEST
		Cldr_name_file file = new Cldr_name_file(lang_key);

		Json_doc jdoc = parser.Parse(json);
		Json_nde root = jdoc.Root_nde();
		int nodes_len = root.Len();
		for (int i = 0; i < nodes_len; i++) {
			Json_kv node = root.Get_at_as_kv(i);
			String key = node.Key_as_str();
			Json_nde val = node.Val_as_nde();

			Ordered_hash files_hash = null;
			if		(String_.Eq(key, Cldr_name_converter.Node_languageNames))	files_hash = file.Language_names();
			else if	(String_.Eq(key, Cldr_name_converter.Node_currencyNames))	files_hash = file.Currency_names();
			else if	(String_.Eq(key, Cldr_name_converter.Node_currencySymbols))	files_hash = file.Currency_symbols();
			else if	(String_.Eq(key, Cldr_name_converter.Node_countryNames))	files_hash = file.Country_names();
			else if	(String_.Eq(key, Cldr_name_converter.Node_timeUnits))		files_hash = file.Time_units();
			else throw Err_.new_unhandled_default(key);
			Load_ary(file, files_hash, val);
		}
		return file; 
	}
	private void Load_ary(Cldr_name_file file, Ordered_hash files_hash, Json_nde nde) {
		int len = nde.Len();
		for (int i = 0; i < len; i++) {
			Json_kv kv = (Json_kv)nde.Get_at(i);
			String key = kv.Key_as_str();
			files_hash.Add(key, Keyval_.new_(key, String_.new_u8(kv.Val_as_bry())));
		}
	}
	private static Hash_adp Make_urls_hash(Io_url[] urls) {
		// filenames will have format of "CldrNamesEn.json" but scrib will pass in "en";
		// this is not an issue for case-insensitive file-systems (WNT), but fails for case-sensitive ones (LNX)
		// build a hash of ("en", "CldrNamesEn.json"); DATE:2018-10-14
		Hash_adp rv = Hash_adp_.New();
		for (Io_url url : urls) {
			String name = url.NameAndExt();
			if (String_.Has_at_bgn(name, Token_cldr_names))
				name = String_.Mid(name, String_.Len(Token_cldr_names), String_.Len(name));
			else {
				Gfo_usr_dlg_.Instance.Warn_many("", "", "file name does not start with " + Token_cldr_names + " ; url=" + url.Raw());
				continue;
			}
			if (String_.Has_at_end(name, Token_json_ext))
				name = String_.Mid(name, 0, String_.Len(name) - String_.Len(Token_json_ext));
			else {
				Gfo_usr_dlg_.Instance.Warn_many("", "", "file name does not end with " + Token_json_ext + " ; url=" + url.Raw());
				continue;
			}
			name = String_.Lower(name);
			name = String_.Replace(name, "_", "-"); // CldrNamesEn_gb.json should have a key of "en-gb", not en_gb; ISSUE#:349; DATE:2019-02-01
			rv.Add_if_dupe_use_1st(name, url);
		}
		return rv;
	}
}
