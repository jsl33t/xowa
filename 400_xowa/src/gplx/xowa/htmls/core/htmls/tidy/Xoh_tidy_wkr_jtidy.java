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
package gplx.xowa.htmls.core.htmls.tidy; import gplx.*; import gplx.xowa.*; import gplx.xowa.htmls.*; import gplx.xowa.htmls.core.*; import gplx.xowa.htmls.core.htmls.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import gplx.core.envs.*;
import gplx.core.envs.*;
class Xoh_tidy_wkr_jtidy implements Xoh_tidy_wkr {
		private Tidy tidy;
	private ByteArrayOutputStream wtr; 
	public void tidy_init() {
		long bgn = System_.Ticks();
		wtr = new ByteArrayOutputStream();
		System.setProperty("line.separator", "\n");
		tidy = new Tidy(); // obtain a new Tidy instance
		tidy.setInputEncoding("utf-8");			// -utf8
		tidy.setOutputEncoding("utf-8");		// -utf8
		tidy.setDocType("\"\"");				// --doctype \"\"; set to empty else some wikis will show paragraph text with little vertical gap; PAGE:tr.b:
		tidy.setForceOutput(true);				// --force-output y 
		tidy.setQuiet(true);					// --quiet y
		tidy.setTidyMark(false);				// --tidy-mark n
		tidy.setWraplen(0);						// --wrap 0
		tidy.setIndentContent(true);			// --indent y; NOTE: true indents all content in edit box
		tidy.setQuoteNbsp(true);				// --quote-nbsp y
		tidy.setLiteralAttribs(true);			// --literal-attributes y
		tidy.setWrapAttVals(false);				// --wrap-attributes n
		tidy.setFixUri(false);					// --fix-url n
		tidy.setFixBackslash(false);			// --fix-backslash n
		tidy.setEncloseBlockText(true);			// --enclose-block-text y; NOTE: true creates extra <p>; very noticeable in sidebar
		tidy.setNumEntities(false);				// NOTE: true will convert all UTF-8 chars to &#val; which ruins readability
		tidy.setTrimEmptyElements(true);		// NOTE: tidy always trims (not even an option)
		tidy.setShowWarnings(false);			// NOTE: otherwise warnings printed to output window
		tidy.setShowErrors(0);					// NOTE: otherwise errors printed to output window; EX: Error: <time> is not recognized!
		app.Usr_dlg().Log_many("", "", "jtidy.init; elapsed=~{0}", System_.Ticks__elapsed_in_frac(bgn));
	}
		private Xoae_app app;
	public byte Tid() {return Xoh_tidy_wkr_.Tid_jtidy;}
	public void Init_by_app(Xoae_app app) {
		this.app = app;
	}
	public void Indent_(boolean v) {
				if (tidy == null) tidy_init();			// lazy create to skip tests
		tidy.setIndentContent(v);
			}
	public void Exec_tidy(Bry_bfr bfr, byte[] page_url) {
				if (tidy == null) tidy_init();			// lazy create to skip tests
//		int bfr_len = bfr.Len();
//		long bgn = Env_.TickCount();
		byte[] orig = bfr.To_bry_and_clear();
		ByteArrayInputStream rdr = new ByteArrayInputStream(orig);
		try {
			tidy.parse(rdr, wtr);
			bfr.Add(wtr.toByteArray());
		}
		catch (Exception exc) {
			bfr.Add(orig);	// jtidy failed; restore original
			app.Usr_dlg().Warn_many("", "", "jtidy.fail; page=~{0} exc=~{1}", page_url, Err_.Message_gplx_full(exc));
		}
		finally {
			wtr.reset();
			try {rdr.close();}
			catch (Exception exc) {System.out.println("jtidy close failed");}
		}
//		app.Usr_dlg().Log_many("", "", "jtidy.exec; elapsed=~{0} len=~{1}", Env_.TickCount_elapsed_in_frac(bgn), bfr_len);
			}
}
