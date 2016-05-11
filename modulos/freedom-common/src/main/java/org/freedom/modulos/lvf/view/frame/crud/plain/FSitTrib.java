/**
 * @version 17/07/2009 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FSitTrib.java <BR>
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

package org.freedom.modulos.lvf.view.frame.crud.plain;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.lvf.view.dialog.report.DLRSitTrib;

public class FSitTrib extends FDados implements ActionListener, RadioGroupListener, CarregaListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodSitTrib = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtDescSitTrib = new JTextFieldPad( JTextFieldPad.TP_STRING, 200, 0 );

	private JTextFieldPad txtImpSitTrib = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );
	
	private JTextFieldPad txtOperacao = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JRadioGroup<String, String> rgTipo = null;

	private JRadioGroup<String, String> rgImpSitTrib = null;

	public FSitTrib() {

		super();
		setTitulo( "Situação Tributária" );
		setAtribos( 50, 50, 434, 250 );

		montaRadioGroup();


		adic( new JLabelPad( "Imposto" ), 7, 0, 403, 20 );
		adic( rgImpSitTrib, 7, 20, 403, 60 );
		//adicDB( rgImpSitTrib, 7, 20, 403, 60, "impsittrib", "Imposto", ListaCampos.DB_PK, true );
		rgImpSitTrib.setBorder( BorderFactory.createEtchedBorder( Color.RED, Color.GRAY ) );
		
		adic( rgTipo, 7, 100, 403, 30, "Operacao");
		
		adicCampo( txtCodSitTrib, 7, 153, 70, 20, "CodSitTrib", "Cód.sit.trib.", ListaCampos.DB_PK, true );
		adicCampoInvisivel( txtImpSitTrib, "impsittrib", "Imposto", ListaCampos.DB_PK, true );
		adicCampoInvisivel( txtOperacao, "Operacao", "Operacao", ListaCampos.DB_SI, true );
		adicCampo( txtDescSitTrib, 80, 153, 330, 20, "DescSitTrib", "Descrição da situação tributária", ListaCampos.DB_SI, true );
		
		setListaCampos( false, "SITTRIB", "LF" );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
		btPrevimp.addActionListener( this );
		btImp.addActionListener( this );
		rgImpSitTrib.addRadioGroupListener( this );
		rgTipo.addRadioGroupListener( this );
		
		lcCampos.addCarregaListener( this );
		lcCampos.addInsertListener( this );

		setImprimir( true );
	}
	
	
	public void montaRadioGroup() {
		
		Vector<String> vImpTribLabs = new Vector<String>();

		vImpTribLabs.addElement( "ICMS" );
		vImpTribLabs.addElement( "IPI" );
		vImpTribLabs.addElement( "PIS" );
		vImpTribLabs.addElement( "COFINS" );
		vImpTribLabs.addElement( "IR" );
		vImpTribLabs.addElement( "Contrib.Social" );
		vImpTribLabs.addElement( "ISS" );

		Vector<String> vImpTribVals = new Vector<String>();
		vImpTribVals.addElement( "IC" );
		vImpTribVals.addElement( "IP" );
		vImpTribVals.addElement( "PI" );
		vImpTribVals.addElement( "CO" );
		vImpTribVals.addElement( "IR" );
		vImpTribVals.addElement( "CS" );
		vImpTribVals.addElement( "IS" );
		
		rgImpSitTrib = new JRadioGroup<String, String>( 2, 3, vImpTribLabs, vImpTribVals );
		
		//Valor inicial do campo impsittrib
		//txtImpSitTrib.setVlrString( "IC" );
		
		
		
		Vector<String> vTipoLabs = new Vector<String>();
		Vector<String> vTipoVals = new Vector<String>();
		vTipoLabs.addElement( "Venda" );
		vTipoLabs.addElement( "Compra" );
		vTipoLabs.addElement( "Ambos" );
		vTipoVals.addElement( "S" );
		vTipoVals.addElement( "E" );
		vTipoVals.addElement( "A" );

		rgTipo = new JRadioGroup<String, String>( 1, 3, vTipoLabs, vTipoVals );
		rgTipo.setVlrString( "A" );
		//Valor inicial do campo operacao
		//txtOperacao.setVlrString("A");
	}
	

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW );
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		DLRSitTrib dl = null;
		ImprimeOS imp = null;
		int linPag = 0;

		dl = new DLRSitTrib();
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Situações Tributárias" );
			imp.limpaPags();

			sSQL = "SELECT CODSITTRIB,DESCSITTRIB,IMPSITTRIB FROM LFSITTRIB WHERE CODEMP=? AND CODFILIAL=? " + "ORDER BY " + dl.getValor();

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFSITTRIB" ) );
			rs = ps.executeQuery();
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 2, "Código" );
					imp.say( imp.pRow(), 30, "Descrição" );
					imp.say( imp.pRow(), 90, "Imposto" );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
				}
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 2, rs.getString( "Codsittrib" ) );
				imp.say( imp.pRow(), 30, rs.getString( "Descsittrib" ) );
				imp.say( imp.pRow(), 90, rs.getString( "impsittrib" ) );
				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();

			imp.fechaGravacao();
			con.commit();
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de Situação Tributária!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			dl = null;
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}

	public void valorAlterado( RadioGroupEvent e ) {

		if ( e.getSource() == rgImpSitTrib ) {
			txtImpSitTrib.setVlrString( rgImpSitTrib.getVlrString() );
		} else if (e.getSource() == rgTipo) {
			txtOperacao.setVlrString( rgTipo.getVlrString() );
		}
	}
	

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcCampos ) {
			rgImpSitTrib.setVlrString( txtImpSitTrib.getVlrString() );
			rgTipo.setVlrString( txtOperacao.getVlrString() );
		}

	}

	public void beforeCarrega( CarregaEvent cevt ) {
	
	}


	public void beforeInsert( InsertEvent ievt ) {

	}


	public void afterInsert( InsertEvent ievt ) {
		if (ievt.getListaCampos() == lcCampos) {
			if ("".equals(txtImpSitTrib.getVlrString())) {
				txtImpSitTrib.setVlrString( rgImpSitTrib.getVlrString() );
			}
			
			if ("".equals(txtOperacao.getVlrString())) {
				txtOperacao.setVlrString( rgTipo.getVlrString() );
			}
		}
	}

}
