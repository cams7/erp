/**
 * @version 10/10/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.tmk <BR>
 *         Classe: @(#)FTipoImp.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.crm.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.DeleteEvent;
import org.freedom.acao.DeleteListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class FTipoImp extends FDados implements PostListener, CarregaListener, DeleteListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodTpImp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescTpImp = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSepTpImp = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtDelCrtTpImp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtLayout = new JTextFieldPad( JTextFieldPad.TP_STRING, 256, 0 );

	private JCheckBoxPad cbTransBinTpImp = new JCheckBoxPad( "Converter arquivo binário", "S", "N" );

	private JCheckBoxPad cbCsepTpImp = new JCheckBoxPad( "Utiliza separador", "S", "N" );

	private JCheckBoxPad cbEnterlnTpImp = new JCheckBoxPad( "Enter quebra linha", "S", "N" );

	private JTablePad tab = new JTablePad();

	private JScrollPane spnLayout = new JScrollPane( tab );

	private JTablePad tabDest = new JTablePad();

	private JScrollPane spnLayoutDest = new JScrollPane( tabDest );

	private JButtonPad btAdic = new JButtonPad( Icone.novo( "btFlechaDir.png" ) );

	private JButtonPad btDel = new JButtonPad( Icone.novo( "btFlechaEsq.png" ) );

	public FTipoImp() {

		super();
		setTitulo( "Cadastro de org.freedom.layout de importação" );
		setAtribos( 50, 20, 497, 390 );

		cbCsepTpImp.setVlrString( "N" );
		cbTransBinTpImp.setVlrString( "N" );

		adicCampo( txtCodTpImp, 7, 20, 80, 20, "CodTpImp", "Cód.tp.imp.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTpImp, 90, 20, 380, 20, "DescTpImp", "Descrição do tipo de importação", ListaCampos.DB_SI, true );
		adicDB( cbCsepTpImp, 7, 45, 130, 20, "CsepTpImp", "", true );
		adicDB( cbEnterlnTpImp, 140, 45, 147, 20, "EnterLnTpImp", "", true );
		adicDB( cbTransBinTpImp, 290, 45, 180, 20, "TransBinTpImp", "", true );
		adicCampo( txtSepTpImp, 7, 85, 80, 20, "SepTpImp", "Separador", ListaCampos.DB_SI, true );
		adicCampo( txtDelCrtTpImp, 90, 85, 120, 20, "DelCrtTpImp", "Ignorar carac. até", ListaCampos.DB_SI, true );
		adicDBLiv( txtLayout, "LayoutTpImp", "Layout", false );
		adic( new JLabelPad( "Compos disponiveis" ), 7, 110, 200, 20 );
		adic( new JLabelPad( "Compos selecionados" ), 260, 110, 200, 20 );
		adic( spnLayout, 7, 130, 213, 180 );
		adic( spnLayoutDest, 260, 130, 213, 180 );
		adic( btAdic, 225, 180, 30, 30 );
		adic( btDel, 225, 215, 30, 30 );
		setListaCampos( true, "TIPOIMP", "TK" );
		lcCampos.setQueryInsert( false );

		tab.adicColuna( "Campo" );
		tab.adicColuna( "Tipo" );
		tabDest.adicColuna( "Campo" );
		tabDest.adicColuna( "Tipo" );

		tab.setTamColuna( 115, 0 );
		tab.setTamColuna( 95, 1 );
		tabDest.setTamColuna( 115, 0 );
		tabDest.setTamColuna( 95, 1 );

		lcCampos.addPostListener( this );
		lcCampos.addCarregaListener( this );
		lcCampos.addDeleteListener( this );

		btAdic.addActionListener( this );
		btDel.addActionListener( this );

	}

	private void adiciona() {

		if ( tab.getNumLinhas() < 1 )
			return;
		for ( int i = tab.getNumLinhas() - 1; i >= 0; i-- ) {
			if ( tab.getSelectedRow() == i ) {
				tabDest.adicLinha( tab.getLinha( i ) );
				tab.tiraLinha( i );
			}
		}
		lcCampos.edit();
	}

	private void remove() {

		if ( tabDest.getNumLinhas() < 1 )
			return;
		for ( int i = tab.getNumLinhas() - 1; i >= 0; i-- ) {
			if ( tabDest.getSelectedRow() == i ) {
				tab.adicLinha( tabDest.getLinha( i ) );
				tabDest.tiraLinha( i );
			}
		}
		lcCampos.edit();
	}

	private void ajustaTabela() {

		String[] sLinhas = txtLayout.getVlrString().split( "," );
		String sSQL = "SELECT * FROM TKCONTATO WHERE 1=0";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData met = rs.getMetaData();
			tab.limpa();
			for ( int i = 1; i <= met.getColumnCount(); i++ ) {
				boolean bColEq = false;
				if ( met.getColumnName( i ).indexOf( "CODEMP" ) >= 0 || met.getColumnName( i ).indexOf( "CODFILIAL" ) >= 0 )
					continue;
				for ( int j = 0; j < sLinhas.length; j++ ) {
					String[] sVals = sLinhas[ j ].split( " " );
					bColEq = met.getColumnName( i ).equals( sVals[ 0 ] );
					if ( bColEq ) {
						tabDest.adicLinha( sVals );
						break;
					}
				}
				if ( !bColEq ) {
					Vector<Object> vVals = new Vector<Object>();
					vVals.add( met.getColumnName( i ) );
					vVals.add( met.getColumnTypeName( i ) );
					tab.adicLinha( vVals );
				}
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar colunas atuais!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btAdic )
			adiciona();
		else if ( evt.getSource() == btDel )
			remove();
		super.actionPerformed( evt );

	}

	public void beforePost( PostEvent pevt ) {

		txtLayout.setText( "" );
		String sSep = "";
		for ( int i = 0; i < tabDest.getNumLinhas(); i++ ) {
			txtLayout.setText( txtLayout.getText() + sSep + tabDest.getValor( i, 0 ) + " " + tab.getValor( i, 1 ) );
			sSep = ",";
		}
	}

	public void afterPost( PostEvent pevt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.ok )
			ajustaTabela();
	}

	public void beforeDelete( DeleteEvent devt ) {

	}

	public void afterDelete( DeleteEvent devt ) {

	}
}
