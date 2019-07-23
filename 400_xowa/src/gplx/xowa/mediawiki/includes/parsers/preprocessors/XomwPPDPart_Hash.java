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
package gplx.xowa.mediawiki.includes.parsers.preprocessors; import gplx.*; import gplx.xowa.*; import gplx.xowa.mediawiki.*; import gplx.xowa.mediawiki.includes.*; import gplx.xowa.mediawiki.includes.parsers.*;
// MW.FILE:Preprocessor_Hash
/**
* @ingroup Parser
*/
// @codingStandardsIgnoreStart Squiz.Classes.ValidClassName.NotCamelCaps
public class XomwPPDPart_Hash extends XomwPPDPart {	// @codingStandardsIgnoreEnd
	private final    Xomw_prepro_accum__hash accum = new Xomw_prepro_accum__hash();
	public XomwPPDPart_Hash(String output) {super(output);
		if (output != String_.Empty) {
			accum.Ary().Add(output);
		}
	}
	@Override public Xomw_prepro_accum Accum() {return accum;}
	@Override public XomwPPDPart Make_new(String val) {
		return new XomwPPDPart_Hash(val);
	}
}