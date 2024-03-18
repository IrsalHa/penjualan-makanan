/*
 * Last Edited By : Irsal Hakim Alamsyah
 * @irsalha
 * Last Edited : 10 - 7 - 2023
 */

package com.vym.utils.helper;

import org.zkoss.zk.ui.util.Clients;

public class MessageBox {

	public static void success(String message) {
		Clients.evalJavaScript(
				"" + "Swal.fire(\r\n" + "  'Success',\r\n" + "  `"+ message +"`,\r\n" + "  'success'\r\n" + ")");
	}
	
	public static void error(String message) {
		Clients.evalJavaScript(
				"" + "Swal.fire(\r\n" + "  'Error',\r\n" + "  `"+ message +"`,\r\n" + "  'error'\r\n" + ")" + "");
	}

	public static void info(String message){
		Clients.evalJavaScript(
				"" + "Swal.fire(\r\n" + "  'Info',\r\n" + "  `"+ message +"`,\r\n" + "  'info'\r\n" + ")" + "");
	}

}
