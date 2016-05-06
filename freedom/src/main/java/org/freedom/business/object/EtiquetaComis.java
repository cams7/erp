/**
 * @version 16/09/2008 <BR>
 * @author Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)ObjetoEtiquetaComis.java <BR>
 * 
 *                              Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                              modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                              na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                              Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                              sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                              Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                              Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                              de acordo com os termos da LPG-PC <BR>
 * <BR>
 */
package org.freedom.business.object;

import org.freedom.library.business.object.Etiqueta;

public class EtiquetaComis extends Etiqueta {

	public EtiquetaComis() {

		adicOpcao( "Código do vendedor", "#CODIGO#", "CODVEND", new Integer( 8 ), null, "Cód.Vend." );
		adicOpcao( "Nome do vendedor", "#NOME#", "NOMEVEND", new Integer( 40 ), null, "Nome Vend." );
		adicOpcao( "Endereço do Vendedor", "#ENDEREÇO#", "ENDVEND", new Integer( 50 ), null, "Endereço" );
		adicOpcao( "Número", "#NUMERO#", "NUMVEND", new Integer( 20 ), null, "Nro." );
		adicOpcao( "Complemento", "#COMPLCLI#", "COMPLCLI", new Integer( 50 ), null, "Complemento" );
		adicOpcao( "Bairro do vendedor", "#BAIRRO#", "BAIRVEND", new Integer( 30 ), null, "Bairro" );
		adicOpcao( "Cidade do vendedor", "#CIDADE#", "CIDVEND", new Integer( 30 ), null, "Cidade" );
		adicOpcao( "CEP", "#CEP#", "CEPVEND", new Integer( 9 ), "#####-###", "Cep" );
		adicOpcao( "UF do Vendedor", "#UF#", "UFVEND", new Integer( 2 ), null, "UF" );

		setNometabela( "VDVENDEDOR" );
		setPK();

	}

	public void setPK() {

		this.PK = "CODVEND";
	}

}
