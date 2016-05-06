/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe:
 * @(#)FTipoConv.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.atd.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.atd.view.dialog.report.DLRTipoConv;

public class FTipoConv extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodTipoConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescTipoConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAuxTipoConv = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtClassOrc = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	public FTipoConv() {

		super();
		nav.setNavigation( true );
		setTitulo( "Cadastro de Tipo de Conveniados" );
		setAtribos( 50, 50, 370, 160 );
		adicCampo( txtCodTipoConv, 7, 20, 80, 20, "CodTpConv", "Cód.tp.conv.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTipoConv, 90, 20, 250, 20, "DescTpConv", "Descrição do tipo de conveniado", ListaCampos.DB_SI, true );
		adicCampo( txtCodAuxTipoConv, 7, 60, 140, 20, "CodAuxTpConv", "Código auxiliar", ListaCampos.DB_SI, false );
		adicCampo( txtClassOrc, 150, 60, 190, 20, "ClassTpConv", "Layout de orçamentos", ListaCampos.DB_SI, false );
		setListaCampos( true, "TIPOCONV", "AT" );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );

		setImprimir( true );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}

		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;

		imp.limpaPags();
		imp.montaCab();
		imp.setTitulo( "Relatório de Tipos de Conveniados" );

		DLRTipoConv dl = new DLRTipoConv();
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}

		String sSQL = "SELECT CODTPCONV,DESCTPCONV FROM ATTIPOCONV ORDER BY " + dl.getValor();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( sSQL );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {

					imp.impCab( 80, false );
					imp.say( 0, imp.normal() );
					imp.say( 2, "Código" );
					imp.say( 25, "Descrição" );
					imp.pulaLinha( 1, imp.normal() );
					imp.say( 0, StringFunctions.replicate( "-", 79 ) );

				}

				imp.pulaLinha( 1, imp.normal() );
				imp.say( 2, rs.getString( "CodTpConv" ) );
				imp.say( 25, rs.getString( "DescTpConv" ) );

			}

			imp.pulaLinha( 1, imp.normal() );
			imp.say( 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();

			imp.fechaGravacao();

			rs.close();
			ps.close();

			con.commit();

			dl.dispose();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de tipos de conveniados!" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}

	}

}
