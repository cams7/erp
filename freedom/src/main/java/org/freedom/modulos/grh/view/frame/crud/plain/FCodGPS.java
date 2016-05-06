/**
 * @version 25/06/2004 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe:
 * @(#)FPais.java <BR>
 * 
 *                Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.grh.view.frame.crud.plain;

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

public class FCodGPS extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodGPS = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescGPS = new JTextFieldPad( JTextFieldPad.TP_STRING, 80, 0 );

	public FCodGPS() {

		super();

		nav.setNavigation( true );
		setTitulo( "Cadastro de Códigos de Pagamento de GPS" );
		setAtribos( 50, 50, 410, 165 );

		lcCampos.setUsaME( false );

		adicCampo( txtCodGPS, 7, 20, 70, 20, "CodGPS", "Cód.GPS", ListaCampos.DB_PK, true );
		adicCampo( txtDescGPS, 80, 20, 280, 20, "DescGPS", "Descrição do código de pagamento GPS/INSS", ListaCampos.DB_SI, true );

		setListaCampos( true, "CODGPS", "RH" );

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

		PreparedStatement ps = null;
		ResultSet rs = null;
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;

		try {

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Códigos de pagamento de GPS/INSS" );

			String sSQL = "SELECT CODGPS,DESCGPS FROM RHCODGPS ORDER BY CODGPS";

			ps = con.prepareStatement( sSQL );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, false );

					imp.say( 0, imp.normal() );
					imp.say( 2, "Cód.GPS" );
					imp.say( 15, "Descrição" );

					imp.pulaLinha( 1, imp.normal() );
					imp.say( 0, StringFunctions.replicate( "-", 135 ) );

				}

				imp.pulaLinha( 1, imp.normal() );
				imp.say( 2, rs.getString( "CodGPS" ) != null ? rs.getString( "CodGPS" ) : "" );
				imp.say( 15, rs.getString( "DescGPS" ) != null ? rs.getString( "DescGPS" ) : "" );

			}

			imp.pulaLinha( 1, imp.normal() );
			imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 135 ) );

			imp.eject();
			imp.fechaGravacao();

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta tabela de codigos de gps!" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}

	}

}
