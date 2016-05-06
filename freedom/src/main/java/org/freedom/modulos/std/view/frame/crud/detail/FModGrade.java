/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FModGrade.java <BR>
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

package org.freedom.modulos.std.view.frame.crud.detail;

import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.std.dao.DAOGrade;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;

public class FModGrade extends FDetalhe implements PostListener, CarregaListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JTextFieldPad txtCodModG = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescProdModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtDescCompModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtRefModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodFabModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodBarModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodItModG = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtOrdemItModG = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtDescItModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodVarG = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtRefItModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldPad txtCodFabItModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodBarItModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescVarG = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextFieldPad txtDescCompItModG = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtPrecoItVarG = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 5 );
	
	private JTextFieldFK txtMostraPossibilidades = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 15, 0 );
	
	private JButtonPad btCopiar = new JButtonPad( Icone.novo( "btCopiar.png" ) );
	
	private JButtonPad btCalcPossibilidades = new JButtonPad( Icone.novo( "btGerar.png" ) );
	
	private ListaCampos lcProd = new ListaCampos( this, "PD" );

	private ListaCampos lcVarG = new ListaCampos( this, "VG" );
	
	private Integer ordem = 1;

	private DAOGrade daoGrade;

	public FModGrade() {

		setTitulo( "Cadastro de Modelos da Grade" );
		setAtribos( 50, 20, 710, 500 );
		setAltCab( 130 );
		pinCab = new JPanelPad( 590, 110 );
		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );

		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, true ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.setWhereAdic( "ATIVOPROD='S'" );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
		lcProd.setQueryCommit( false );
		lcProd.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProd, FProduto.class.getCanonicalName() );

		adicCampo( txtCodModG, 7, 20, 70, 20, "CodModG", "Cód.mod.g.", ListaCampos.DB_PK, true );
		adicCampo( txtDescModG, 80, 20, 197, 20, "DescModG", "Descrição do modelo de grade", ListaCampos.DB_SI, true );
		adicCampo( txtCodProd, 280, 20, 77, 20, "CodProd", "Cód.prod.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescProd, 360, 20, 200, 20, "DescProd", "Descição do produto" );
		adic( btCalcPossibilidades, 563, 20, 30, 30 );
		adic( txtMostraPossibilidades, 596, 20, 80, 20, "Combinações" );
		
		adicCampo( txtDescProdModG, 7, 60, 144, 20, "DescProdModG", "Desc. inicial", ListaCampos.DB_SI, true );
		adicCampo( txtDescCompModG, 154, 60, 187, 20, "DescCompProdModG", "Descrição completa inicial", ListaCampos.DB_SI, false );
		
		
		adicCampo( txtRefModG, 344, 60, 70, 20, "RefModG", "Ref.inic.", ListaCampos.DB_SI, true );
		adicCampo( txtCodFabModG, 417, 60, 70, 20, "CodFabModG", "Cód.fab.inic.", ListaCampos.DB_SI, true );
		adicCampo( txtCodBarModG, 490, 60, 70, 20, "CodBarModG", "Cód.bar.inic.", ListaCampos.DB_SI, true );
		adic( btCopiar, 563, 55, 30, 30 );
		setListaCampos( true, "MODGRADE", "EQ" );
		setAltDet( 100 );
		pinDet = new JPanelPad( 590, 110 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );

		lcVarG.add( new GuardaCampo( txtCodVarG, "CodVarG", "Cód.var.g.", ListaCampos.DB_PK, true ) );
		lcVarG.add( new GuardaCampo( txtDescVarG, "DescVarG", "Descrição da variante", ListaCampos.DB_SI, false ) );
		lcVarG.montaSql( false, "VARGRADE", "EQ" );
		lcVarG.setQueryCommit( false );
		lcVarG.setReadOnly( true );
		txtCodVarG.setTabelaExterna( lcVarG, null );

		adicCampo( txtCodItModG, 7, 20, 70, 20, "CodItModG", "Item", ListaCampos.DB_PK, true );
		adicCampo( txtOrdemItModG, 80, 20, 70, 20, "OrdemItModG", "Ordem", ListaCampos.DB_SI, true );
		adicCampo( txtCodVarG, 153, 20, 77, 20, "CodVarG", "Cód.var.g.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescVarG, 233, 20, 150, 20, "DescVarG", "Descrição da variante" );
		adicCampo( txtDescItModG, 386, 20, 150, 20, "DescItModG", "Descrição", ListaCampos.DB_SI, true );
		adicCampo( txtRefItModG, 7, 60, 87, 20, "RefItModG", "Ref.inicial", ListaCampos.DB_SI, true );
		adicCampo( txtCodFabItModG, 100, 60, 87, 20, "CodFabItModG", "Cód.fab.inic.", ListaCampos.DB_SI, true );
		adicCampo( txtCodBarItModG, 190, 60, 100, 20, "CodBarItModG", "Cód.bar.inic.", ListaCampos.DB_SI, true );
		adicCampo( txtDescCompItModG, 293, 60, 267, 20, "DescCompItModG", "Descrição completa do item", ListaCampos.DB_SI, false );
		adicCampo( txtPrecoItVarG, 563, 60, 80, 20, "PrecoItVarG", "Preço", ListaCampos.DB_SI, false );
		
		
		lcDet.setOrdem( "OrdemItModG" );
		setListaCampos( true, "ITMODGRADE", "EQ" );
		
		montaTab();
		
		lcCampos.addCarregaListener( this );
		lcDet.addPostListener( this );
		lcDet.addCarregaListener( this );
		lcDet.addInsertListener( this ); 
		btCopiar.addActionListener( this );
		btCalcPossibilidades.addActionListener( this );
		
		btCalcPossibilidades.setToolTipText( "Calcular combinacoes" );
		btCopiar.setToolTipText( "Copiar modelo de grade" );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcProd.setConexao( cn );
		lcVarG.setConexao( cn );
		txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
		
		
		daoGrade = new DAOGrade( cn );
	}
	
	@ Override
	public void afterPost( PostEvent pevt ) {
		if(pevt.getListaCampos() == lcDet ) {
			if(ordem != txtOrdemItModG.getVlrInteger()) {
				try {
					ordenaGrid( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQITMODGRADE" ), txtCodModG.getVlrInteger(), txtCodItModG.getVlrInteger(), txtOrdemItModG.getVlrInteger() );	
				} catch (SQLException e) {
					Funcoes.mensagemErro( this, "Erro ao alterar ordenação da grid!!!" );
					e.printStackTrace();
				}			
				lcCampos.carregaDados();
			}
		}
	
		super.afterPost( pevt );
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub
		
	}

	public void afterCarrega( CarregaEvent cevt ) {
		if(cevt.getListaCampos() == lcCampos ) {
			txtMostraPossibilidades.setVlrInteger( 0 );
		}
		
		if(cevt.getListaCampos() == lcDet ) {
			ordem = txtOrdemItModG.getVlrInteger();
		}
	}
	
	private void ordenaGrid(Integer codemp, Integer codfilial, Integer codmodg, Integer coditmodg, int ordemitmodg) throws SQLException {
		StringBuilder sql = new StringBuilder();
		StringBuilder update = new StringBuilder();

		sql.append( "select im.codemp, im.codfilial, im.codmodg, im.coditmodg ");
		sql.append( "from eqitmodgrade im ");
		sql.append( "where im.ordemitmodg>=? and im.coditmodg<>? ");
		sql.append( "and im.codemp=? and im.codfilial=? and im.codmodg=? ");
		sql.append( "order by im.ordemitmodg ");
		
		update.append( "update eqitmodgrade imu set imu.ordemitmodg=? ");
		update.append( "where imu.codemp=? and imu.codfilial=? and imu.codmodg=? ");
		update.append( "and imu.coditmodg=? and imu.ordemitmodg<>? ");

		
		int param = 1;
		PreparedStatement ps = con.prepareStatement( sql.toString() );
		ps.setInt( param++, ordemitmodg );
		ps.setInt( param++, coditmodg );
		ps.setInt( param++, codemp );
		ps.setInt( param++, codfilial );
		ps.setInt( param++, codmodg );
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			ordemitmodg ++;
			param = 1;
			PreparedStatement psUpdate = con.prepareStatement( update.toString() );
			
			psUpdate.setInt( param++, ordemitmodg );
			psUpdate.setInt( param++, codemp );
			psUpdate.setInt( param++, codfilial );
			psUpdate.setInt( param++, codmodg );
			psUpdate.setInt( param++, rs.getInt( "coditmodg" ) );
			psUpdate.setInt( param++, ordemitmodg );
			psUpdate.execute();
			psUpdate.close();
		}
		
		rs.close();
		ps.close();
		con.commit();
	}
	
	public void actionPerformed( ActionEvent evt ) {
		super.actionPerformed( evt );
		
		if ( evt.getSource() == btCopiar ) {
			copiaModGrade();
		} else if (evt.getSource() == btCalcPossibilidades) {
			calculaCombinacoes();
		}
		
	}

	private void calculaCombinacoes() {

		txtMostraPossibilidades.setVlrInteger( daoGrade.calculaCombinacoes( Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQITMODGRADE" ), txtCodModG.getVlrInteger() ) );
		
	}

	private void copiaModGrade() {
		if( txtCodModG.getVlrInteger() != 0) {
			if (Funcoes.mensagemConfirma( this, "Deseja copiar o Modelo de Grade?" ) == JOptionPane.YES_OPTION) {
				int novoCodModG = daoGrade.copiaModGrade(Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "EQMODGRADE" ), txtCodModG.getVlrInteger());
				if( novoCodModG != 0) {
					if (Funcoes.mensagemConfirma( this, "Modelo de grade: " + novoCodModG + "\nGerado com sucesso, deseja Edita-lo? " ) == JOptionPane.YES_OPTION) {
						txtCodModG.setVlrInteger(novoCodModG);
						lcCampos.carregaDados();
					}
				}
			}
		 } else {
			 Funcoes.mensagemInforma( this, "Selecione um Modelo de Grade" );
		 }
		
	}

	public void beforeInsert( InsertEvent ievt ) {

	}

	public void afterInsert( InsertEvent ievt ) {
		if (ievt.getListaCampos() == lcDet) {
			if (txtOrdemItModG.getVlrInteger() == 0) {
				txtOrdemItModG.setVlrInteger(txtCodItModG.getVlrInteger());
				txtCodVarG.requestFocus();
			}
		}
	}
	
}
