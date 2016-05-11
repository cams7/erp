/**
 * @version 24/03/2004 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.std <BR>
 * Classe:
 * @(#)FRVendaSetor.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *  Tela para extração do relatório de vendas por setor.
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;


public class FRVendaSetor extends FRelatorio implements RadioGroupListener {

	private static final long serialVersionUID = 1L;

	private final int POS_CODSETOR = 0;

	private final int POS_MES = 1;

	private final int POS_CODGRUP = 2;

	private final int POS_SIGLAGRUP = 3;

	private final int POS_CODVEND = 4;

	private final int POS_VALOR = 5;

	private final int POS_TOTSETOR = 6;

	private final int TAM_GRUPO = 14;

	private final int NUM_COLUNAS = 9;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldFK txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrup1 = new JTextFieldPad( JTextFieldPad.TP_STRING, TAM_GRUPO, 0 );

	private JTextFieldFK txtDescGrup1 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrup2 = new JTextFieldPad( JTextFieldPad.TP_STRING, TAM_GRUPO, 0 );

	private JTextFieldFK txtDescGrup2 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtSiglaMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCodSetor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescSetor = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodTipoCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldPad txtCodCFOP = new JTextFieldPad( JTextFieldPad.TP_STRING, 5, 0 );

	private JTextFieldFK txtDescCFOP = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbMovEstoque = new JCheckBoxPad( "Só com mov.estoque?", "S", "N" );

	private JCheckBoxPad cbCliPrinc = new JCheckBoxPad( "Mostrar no cliente principal?", "S", "N" );

	private JCheckBoxPad cbVendaCanc = new JCheckBoxPad( "Mostrar Canceladas", "S", "N" );

	private JRadioGroup<?, ?> rgFaturados = null;

	private JRadioGroup<?, ?> rgFinanceiro = null;

	private JRadioGroup<?, ?> rgTipoRel = null;

	private JRadioGroup<?, ?> rgOrdemRel = null;
	
	private JRadioGroup<?, ?> rgTipo = null;
	
	private JRadioGroup<?, ?> rgTipoDet = null;

	private Vector<String> vLabsFat = new Vector<String>();

	private Vector<String> vValsFat = new Vector<String>();

	private Vector<String> vLabsFin = new Vector<String>();

	private Vector<String> vValsFin = new Vector<String>();

	private Vector<String> vLabTipoRel = new Vector<String>();

	private Vector<String> vValTipoRel = new Vector<String>();

	private Vector<String> vLabOrdemRel = new Vector<String>();

	private Vector<String> vValOrdemRel = new Vector<String>();

	private ListaCampos lcGrup1 = new ListaCampos( this );

	private ListaCampos lcGrup2 = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	private ListaCampos lcSetor = new ListaCampos( this );

	private ListaCampos lcVendedor = new ListaCampos( this );

	private ListaCampos lcCliente = new ListaCampos( this );

	private ListaCampos lcTipoCli = new ListaCampos( this );
	
	private ListaCampos lcCFOP = new ListaCampos( this );
	
	private ListaCampos lcMov = new ListaCampos( this );
	
	private JRadioGroup<?, ?> rgEmitidos = null;
	
	private Vector<String> vLabsEmit = new Vector<String>();

	private Vector<String> vValsEmit = new Vector<String>();
	
	private Vector<String> vLabsGraf = new Vector<String>();
	
	private Vector<String> vValsGraf = new Vector<String>();
	
	private Vector<String> vLabsGrafResu = new Vector<String>();
	
	private Vector<String> vValsGrafResu = new Vector<String>();
	
	private boolean bPref = false;
	
	private JCheckBoxPad cbPorConserto = new JCheckBoxPad( "Por item de O.S", "S", "N" );

	public FRVendaSetor() {

		setTitulo( "Relatório de Vendas por Setor" );
		setAtribos( 80, 80, 660, 570 );
	
		GregorianCalendar cal = new GregorianCalendar();
		cal.add( Calendar.DATE, -30 );
		txtDataini.setVlrDate( cal.getTime() );
		cal.add( Calendar.DATE, 30 );
		txtDatafim.setVlrDate( cal.getTime() );
		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		cbMovEstoque.setVlrString( "N" );
		cbCliPrinc.setVlrString( "S" );
		
		vLabsGraf.addElement( "Grafico" );
		vLabsGraf.addElement( "Texto" );
		vValsGraf.addElement( "G" );
		vValsGraf.addElement( "T" );
		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabsGraf, vValsGraf );
		rgTipo.setVlrString( "T" );
		
		vLabsGrafResu.addElement( "Resumido" );
		vLabsGrafResu.addElement( "Detalhado" );
		vValsGrafResu.addElement( "R" );
		vValsGrafResu.addElement( "D" );
		rgTipoDet = new JRadioGroup<String, String>( 1, 2, vLabsGrafResu, vValsGrafResu );
		rgTipoDet.setVlrString( "R" );

		vLabsFat.addElement( "Faturado" );
		vLabsFat.addElement( "Não Faturado" );
		vLabsFat.addElement( "Ambos" );
		vValsFat.addElement( "S" );
		vValsFat.addElement( "N" );
		vValsFat.addElement( "A" );
		rgFaturados = new JRadioGroup<String, String>( 3, 1, vLabsFat, vValsFat );
		rgFaturados.setVlrString( "S" );

		vLabsFin.addElement( "Financeiro" );
		vLabsFin.addElement( "Não Finaceiro" );
		vLabsFin.addElement( "Ambos" );
		vValsFin.addElement( "S" );
		vValsFin.addElement( "N" );
		vValsFin.addElement( "A" );
		rgFinanceiro = new JRadioGroup<String, String>( 3, 1, vLabsFin, vValsFin );
		rgFinanceiro.setVlrString( "S" );

		vLabTipoRel.addElement( "Vendedor" );
		vLabTipoRel.addElement( "Produto" );
		vLabTipoRel.addElement( "Cliente" );
		vValTipoRel.addElement( "V" );
		vValTipoRel.addElement( "P" );
		vValTipoRel.addElement( "C" );
		rgTipoRel = new JRadioGroup<String, String>( 1, 3, vLabTipoRel, vValTipoRel );
		rgTipoRel.addRadioGroupListener( this );

		vLabOrdemRel.addElement( "Valor" );
		vLabOrdemRel.addElement( "Razão social" );
		vLabOrdemRel.addElement( "Cód.cli." );
		vValOrdemRel.addElement( "V" );
		vValOrdemRel.addElement( "R" );
		vValOrdemRel.addElement( "C" );
		rgOrdemRel = new JRadioGroup<String, String>( 3, 1, vLabOrdemRel, vValOrdemRel );

		vLabsEmit.addElement( "Emitidos" );
		vLabsEmit.addElement( "Não emitidos" );
		vLabsEmit.addElement( "Ambos" );
		vValsEmit.addElement( "S" );
		vValsEmit.addElement( "N" );
		vValsEmit.addElement( "A" );
		rgEmitidos = new JRadioGroup<String, String>( 3, 1, vLabsEmit, vValsEmit );
		rgEmitidos.setVlrString( "A" );
		
		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.add( new GuardaCampo( txtSiglaMarca, "SiglaMarca", "Sigla", ListaCampos.DB_SI, false ), "txtSiglaMarca" );
		lcMarca.montaSql( false, "MARCA", "EQ" );
		lcMarca.setReadOnly( true );
		txtCodMarca.setTabelaExterna( lcMarca, null );
		txtCodMarca.setFK( true );
		txtCodMarca.setNomeCampo( "CodMarca" );

		lcGrup1.add( new GuardaCampo( txtCodGrup1, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup1.add( new GuardaCampo( txtDescGrup1, "DescGrup", "Descrição do gurpo", ListaCampos.DB_SI, false ) );
		lcGrup1.montaSql( false, "GRUPO", "EQ" );
		lcGrup1.setReadOnly( true );
		txtCodGrup1.setTabelaExterna( lcGrup1, null );
		txtCodGrup1.setFK( true );
		txtCodGrup1.setNomeCampo( "CodGrup" );

		lcGrup2.add( new GuardaCampo( txtCodGrup2, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup2.add( new GuardaCampo( txtDescGrup2, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup2.montaSql( false, "GRUPO", "EQ" );
		lcGrup2.setReadOnly( true );
		txtCodGrup2.setTabelaExterna( lcGrup2, null );
		txtCodGrup2.setFK( true );
		txtCodGrup2.setNomeCampo( "CodGrup" );

		lcSetor.add( new GuardaCampo( txtCodSetor, "CodSetor", "Cód.setor", ListaCampos.DB_PK, false ) );
		lcSetor.add( new GuardaCampo( txtDescSetor, "DescSetor", "Descrição do setor", ListaCampos.DB_SI, false ) );
		lcSetor.montaSql( false, "SETOR", "VD" );
		lcSetor.setReadOnly( true );
		txtCodSetor.setTabelaExterna( lcSetor, null );
		txtCodSetor.setFK( true );
		txtCodSetor.setNomeCampo( "CodSetor" );

		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "VD" );
		lcVendedor.setReadOnly( true );
		txtCodVend.setTabelaExterna( lcVendedor, null );
		txtCodVend.setFK( true );
		txtCodVend.setNomeCampo( "CodVend" );

		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCliente, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCliente.setReadOnly( true );
		lcCliente.montaSql( false, "CLIENTE", "VD" );

		lcTipoCli.add( new GuardaCampo( txtCodTipoCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, false ) );
		lcTipoCli.add( new GuardaCampo( txtDescTipoCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false ) );
		txtCodTipoCli.setTabelaExterna( lcTipoCli, null );
		txtCodTipoCli.setNomeCampo( "CodTipoCli" );
		txtCodTipoCli.setFK( true );
		lcTipoCli.setReadOnly( true );
		lcTipoCli.montaSql( false, "TIPOCLI", "VD" );
		
		
		lcCFOP.add( new GuardaCampo( txtCodCFOP, "CodNat", "CFOP", ListaCampos.DB_PK, false ) );
		lcCFOP.add( new GuardaCampo( txtDescCFOP, "DescNat", "Descrição da CFOP", ListaCampos.DB_SI, false ) );
		lcCFOP.montaSql( false, "NATOPER", "LF" );
		lcCFOP.setQueryCommit( false );
		lcCFOP.setReadOnly( true );
		txtCodCFOP.setNomeCampo( "CodNat" );
		txtCodCFOP.setFK( true );
		txtCodCFOP.setTabelaExterna( lcCFOP, null );

		lcMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcMov.montaSql( false, "TIPOMOV", "EQ" );
		lcMov.setQueryCommit( false );
		lcMov.setReadOnly( true );
		txtCodTipoMov.setNomeCampo( "CodTipoMov" );
		txtCodTipoMov.setFK( true );
		txtCodTipoMov.setTabelaExterna( lcMov, null );

		adic( new JLabelPad( "Modo de impressão" ), 7, 0, 200, 20 );
		adic( rgTipoRel, 7, 20, 281, 30 );

		adic( new JLabelPad( "Ordem" ), 303, 0, 70, 20 );
		adic( rgOrdemRel, 303, 20, 283, 77 );

		adic( new JLabelPad( "Período" ), 7, 50, 100, 20 );
		adic( txtDataini, 7, 70, 120, 20 );
		adic( new JLabelPad( "Até" ), 138, 70, 40, 20 );
		adic( txtDatafim, 168, 70, 100, 20 );

		adic( new JLabelPad( "Cód.marca" ), 7, 100, 190, 20 );
		adic( txtCodMarca, 7, 120, 90, 20 );
		adic( new JLabelPad( "Descrição da marca" ), 100, 100, 190, 20 );
		adic( txtDescMarca, 100, 120, 190, 20 );
		adic( new JLabelPad( "Cód.gp/somar" ), 7, 140, 200, 20 );
		adic( txtCodGrup1, 7, 160, 90, 20 );
		adic( new JLabelPad( "Descrição do grupo/somar" ), 100, 140, 190, 20 );
		adic( txtDescGrup1, 100, 160, 190, 20 );
		adic( new JLabelPad( "Cód.gp/subtrair" ), 7, 180, 200, 20 );
		adic( txtCodGrup2, 7, 200, 90, 20 );
		adic( new JLabelPad( "Descrição do grupo/subtrair" ), 100, 180, 190, 20 );
		adic( txtDescGrup2, 100, 200, 190, 20 );

		adic( new JLabelPad( "Cód.setor" ), 303, 100, 90, 20 );
		adic( txtCodSetor, 303, 120, 90, 20 );
		adic( new JLabelPad( "Descrição do setor" ), 396, 100, 190, 20 );
		adic( txtDescSetor, 396, 120, 190, 20 );
		adic( new JLabelPad( "Cód.comiss." ), 303, 140, 190, 20 );
		adic( txtCodVend, 303, 160, 90, 20 );
		adic( new JLabelPad( "Nome do comissionado" ), 396, 140, 190, 20 );
		adic( txtNomeVend, 396, 160, 190, 20 );
		adic( new JLabelPad( "Cód.cli." ), 303, 180, 90, 20 );
		adic( txtCodCli, 303, 200, 90, 20 );
		adic( new JLabelPad( "Razão social do cliente" ), 396, 180, 190, 20 );
		adic( txtRazCli, 396, 200, 190, 20 );
		adic( new JLabelPad( "Cód.tp.cli." ), 303, 220, 90, 20 );
		adic( txtCodTipoCli, 303, 240, 90, 20 );
		adic( new JLabelPad( "Descrição do tipo de cliente" ), 396, 220, 190, 20 );
		adic( txtDescTipoCli, 396, 240, 190, 20 );
		adic( new JLabelPad( "Cód.CFOP" ), 303, 260, 90, 20 );
		adic( txtCodCFOP, 303, 280, 90, 20 );
		adic( new JLabelPad( "Descrição da CFOP" ), 396, 260, 190, 20 );
		adic( txtDescCFOP, 396, 280, 190, 20 );
		adic( new JLabelPad( "Cód.tp.mov." ), 303, 300, 90, 20 );
		adic( txtCodTipoMov, 303, 320, 90, 20 );
		adic( new JLabelPad( "Descrição do tipo de movimento" ), 396, 300, 190, 20 );
		adic( txtDescTipoMov, 396, 320, 190, 20 );

		
		adic( rgTipo, 7,230,283,30);
		adic( rgTipoDet, 7,270,283,30);
				
		adic( rgFaturados, 7, 310, 130, 70 );		
		adic( rgFinanceiro, 160, 310, 130, 70 );
		adic( rgEmitidos, 7, 390, 130, 70 );
		
		adic( cbMovEstoque, 160, 390, 200, 20 );
		adic( cbCliPrinc, 160, 410, 300, 20 );
		adic( cbVendaCanc, 160, 430, 200, 20 );
		adic( cbPorConserto,  160,	450, 	200, 	20 );			


	}

	public void imprimir( TYPE_PRINT bVisualizar ) {
		
		Blob fotoemp = null;

		if ( txtDataini.getVlrString().length() < 10 || txtDatafim.getVlrString().length() < 10 ) {

			Funcoes.mensagemInforma( this, "Período inválido!" );
			return;
		}

		if ( "G".equals(rgTipo.getVlrString()) ) {
			try {
				PreparedStatement ps = con.prepareStatement( "SELECT FOTOEMP FROM SGEMPRESA WHERE CODEMP=?" );
				ps.setInt( 1, Aplicativo.iCodEmp );
				
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					fotoemp = rs.getBlob( "FOTOEMP" );
				}
				rs.close();
				ps.close();
				con.commit();
				
			} catch (Exception e) {
				Funcoes.mensagemErro( this, "Erro carregando logotipo.\n" + e.getMessage() );
				e.printStackTrace();
			}						
		}

		if ( "V".equals( rgTipoRel.getVlrString() ) ) {

			impVendedor( bVisualizar, "G".equals(rgTipo.getVlrString()) );
		}
		else if ( "P".equals( rgTipoRel.getVlrString() ) ) {
			
			if("S".equals( cbPorConserto.getVlrString() )) {
				impConserto( bVisualizar, "G".equals(rgTipo.getVlrString()) );
			}
			else {
				impProduto( bVisualizar, "G".equals(rgTipo.getVlrString()) );
			}
			
		}
		else if ( "C".equals( rgTipoRel.getVlrString() ) ) {

			impCliente( bVisualizar, "G".equals(rgTipo.getVlrString()), "D".equals(rgTipoDet.getVlrString()), fotoemp );
		}
		
		
		
	}

	private int getPassadas( int iTam ) {

		int iRetorno = 0;
		iRetorno = iTam / 9;

		if ( ( iTam % 9 ) > 0 ) {

			iRetorno++;
		}

		return iRetorno;
	}

	private Vector<String> getVendedores( String sCodSetor, Vector<Vector<Object>> vItens ) {

		Vector<String> vRetorno = new Vector<String>();
		String sCodVend = "";
		boolean bInicio = false;

		for ( int i = 0; i < vItens.size(); i++ ) {

			if ( vItens.elementAt( i ).elementAt( POS_CODSETOR ).toString().equals( sCodSetor ) ) {

				bInicio = true;
				sCodVend = new String( vItens.elementAt( i ).elementAt( POS_CODVEND ).toString() );

				if ( vRetorno.indexOf( sCodVend ) == -1 ) {

					vRetorno.addElement( sCodVend );
				}
			}
			else if ( bInicio ) {

				break;
			}
		}

		vRetorno = Funcoes.ordenaVector( vRetorno, 8 );

		return vRetorno;
	}

	private int getPosCol( int iColSel ) {

		int iRetorno = 0;

		if ( iColSel < NUM_COLUNAS ) { // Verifica se a coluna selecionada é menor que o número de colunas total

			iRetorno = iColSel;
		}
		else { // caso contrário a retorna o resto

			iRetorno = iColSel % NUM_COLUNAS;
		}

		return iRetorno;
	}

	private String getColSetor( final Vector<String> vCols, final int iTotPassadas, final int iPassada ) {

		StringBuffer sRetorno = new StringBuffer();
		int iCols = 0;
		int iColunas = NUM_COLUNAS;

		if ( ( iTotPassadas - 1 ) == iPassada ) {

			iColunas = vCols.size() % NUM_COLUNAS;

			if ( iColunas == 0 ) {

				iColunas = NUM_COLUNAS;
			}
		}

		for ( int i = 0; i < iColunas; i++ ) {

			sRetorno.append( "| " );
			sRetorno.append( Funcoes.adicionaEspacos( vCols.elementAt( i + ( iPassada * NUM_COLUNAS ) ).toString(), NUM_COLUNAS ) );
			iCols = i;
		}

		for ( int i = iCols; i < ( NUM_COLUNAS - 1 ); i++ ) {

			sRetorno.append( "|" );
			sRetorno.append( StringFunctions.replicate( " ", 10 ) );
		}

		sRetorno.append( StringFunctions.replicate( " ", 102 - sRetorno.length() ) );
		sRetorno.append( iPassada == 0 ? "| TOTAL" : "|      " );

		return sRetorno.toString();
	}

	private String getTotSetor( Vector<Double> vTotSetor, int iTotPassadas, int iPassada ) {

		StringBuffer sRetorno = new StringBuffer();
		double deTotal = 0;
		Double deTemp = null;
		int iCols = 0;
		int iColunas = NUM_COLUNAS;

		if ( ( iTotPassadas - 1 ) == iPassada ) {

			iColunas = vTotSetor.size() % NUM_COLUNAS;

			if ( iColunas == 0 ) {

				iColunas = NUM_COLUNAS;
			}
		}

		for ( int i = 0; i < iColunas; i++ ) {

			deTemp = vTotSetor.elementAt( i + ( iPassada * NUM_COLUNAS ) );

			if ( deTemp == null ) {

				deTemp = new Double( 0 );
			}

			deTotal += deTemp.doubleValue();
			sRetorno.append( "|" );
			sRetorno.append( Funcoes.strDecimalToStrCurrency( 10, 2, deTemp.toString() ) );
			iCols = i;
		}

		for ( int i = iCols; i < ( NUM_COLUNAS - 1 ); i++ ) {

			sRetorno.append( "|" );
			sRetorno.append( StringFunctions.replicate( " ", 10 ) );
		}

		sRetorno.append( StringFunctions.replicate( " ", 102 - sRetorno.length() ) );
		sRetorno.append( "|" );
		sRetorno.append( ( ( iTotPassadas - 1 ) == iPassada ? Funcoes.strDecimalToStrCurrency( 11, 2, deTotal + "" ) : StringFunctions.replicate( " ", 11 ) ) );

		return sRetorno.toString();

	}

	private int posVendedor( String sCodVend, Vector<String> vCols ) {

		int iRetorno = -1;

		if ( vCols != null ) {

			for ( int i = 0; i < vCols.size(); i++ ) {

				if ( sCodVend.equals( vCols.elementAt( i ).toString() ) ) {

					iRetorno = i;
					break;
				}
			}
		}

		return iRetorno;
	}

	private Vector<Double> initTotSetor( Vector<String> vCols ) {

		Vector<Double> vRetorno = new Vector<Double>();

		for ( int i = 0; i < vCols.size(); i++ ) {

			vRetorno.addElement( new Double( 0 ) );
		}

		return vRetorno;
	}

	private Vector<Double> adicValorSetor( int iPos, Double deValor, Vector<Double> vTotSetor ) {

		Double dlTemp = null;
		double dTemp = 0;

		if ( ( vTotSetor != null ) && ( deValor != null ) ) {

			if ( iPos < vTotSetor.size() ) {

				dlTemp = vTotSetor.elementAt( iPos );

				if ( dlTemp != null ) {

					dTemp = dlTemp.doubleValue() + deValor.doubleValue();
					dlTemp = new Double( dTemp );
					vTotSetor.setElementAt( dlTemp, iPos );
				}
			}
		}

		return vTotSetor;
	}

	private void impVendedor( TYPE_PRINT bVisualizar, boolean postscript ) {

		if (postscript) {
			Funcoes.mensagemInforma( this, "Relatório gráfico não disponível para modo de impressão vendedor !" );
			return;
		}
		if ("D".equals(rgTipoDet.getVlrString() )) {
			Funcoes.mensagemInforma( this, "Relatório Detalhado não disponível para modo texto !" );
			return;
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sWhere4 = "";
		String sWhereTM = "";
		String sCab = "";
		String sFrom = "";
		StringBuffer sFiltros1 = new StringBuffer();
		StringBuffer sFiltros2 = new StringBuffer();
		String sCodMarca = null;
		String sCodGrup1 = null;
		String sCodGrup2 = null;
		String sSiglaGroup = null;
		String sCodSetor = null;
		String sCodSetorAnt = null;
		String sCodGrupAnt = null;
		String sCodGrup = null;
		String sMes = null;
		String sMesAnt = null;
		ImprimeOS imp = null;
		Vector<Vector<Object>> vItens = null;
		Vector<Object> vItem = null;
		Vector<Double> vTotSetor = null;
		Vector<String> vCols = null;
		int linPag = 0;
		int iParam = 1;
		int iCol = 0;
		int iPosCol = 0;
		int iPosColAnt = 0;
		int iPos = 0;
		int iLinsSetor = 0;
		int iCodSetor = 0;
		int iCodCli = 0;
		int iCodTipoCli = 0;
		int iCodVend = 0;
		int iTotPassadas = 0;
		double deValor = 0;
		double deTotal1 = 0;
		double deTotalGeral = 0;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.setTitulo( "Relatorio de Vendas por Setor" );
			imp.montaCab();
			imp.addSubTitulo( "VENDAS POR SETOR" );
			imp.addSubTitulo( "PERIODO DE: " + txtDataini.getVlrString() + " ATE: " + txtDatafim.getVlrString() );
			imp.limpaPags();

			vItens = new Vector<Vector<Object>>();
			sCodMarca = txtCodMarca.getVlrString().trim();
			sCodGrup1 = txtCodGrup1.getVlrString().trim();
			sCodGrup2 = txtCodGrup2.getVlrString().trim();
			iCodSetor = txtCodSetor.getVlrInteger().intValue();
			iCodVend = txtCodVend.getVlrInteger().intValue();
			iCodCli = txtCodCli.getVlrInteger().intValue();
			iCodTipoCli = txtCodTipoCli.getVlrInteger().intValue();
			sCodSetorAnt = "";
			sCodSetor = "";
			sCodGrupAnt = "";
			sCodGrup = "";
			sMes = "";
			sMesAnt = "";

			if ( rgFaturados.getVlrString().equals( "S" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab = "SO FATURADO";
			}
			else if ( rgFaturados.getVlrString().equals( "N" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				sCab = "NAO FATURADO";
			}
			else if ( rgFaturados.getVlrString().equals( "A" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}
			
			if ( txtCodCFOP.getVlrInteger().intValue() > 0 ) {
				sWhere1 += " AND IV.CODNAT=" + txtCodCFOP.getVlrInteger().intValue();
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( " CFOP.: " );
				sFiltros1.append( txtDescCFOP.getText().trim() );
			}
			if ( txtCodTipoMov.getVlrInteger().intValue() > 0 ) {
				sWhere1 += " AND V.CODTIPOMOV=" + txtCodTipoMov.getVlrInteger().intValue();
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( " TipoMov.: " );
				sFiltros1.append( txtDescTipoMov.getText().trim() );
			}

			if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				sCab += sCab.length() > 0 ? " - SO FINANCEIRO" : "SO FINANCEIRO";
			}
			else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				sCab += sCab.length() > 0 ? " - NAO FINANCEIRO" : "NAO FINANCEIRO";
			}
			else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}

			if ( cbMovEstoque.getVlrString().equals( "S" ) ) {
				sFiltros1.append( "SO MOV.ESTOQUE" );
				sWhereTM = cbMovEstoque.getVlrString().equals( "S" ) ? " AND TM.ESTOQTIPOMOV='S' " : "";
			}
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere4 = " AND V.STATUSVENDA IN ('V2','V3','P3') " ;
				sFiltros1.append( " EMITIDOS " );
			}

			if ( !sCodMarca.equals( "" ) ) {
				sWhere.append( "AND P.CODEMPMC=? AND P.CODFILIALMC=? AND P.CODMARCA=? " );
				sFiltros1.append( sFiltros1.length() > 0 ? " / " : "" );
				sFiltros1.append( "M.: " );
				sFiltros1.append( txtDescMarca.getText().trim() );
			}
			if ( !sCodGrup1.equals( "" ) ) {
				sWhere.append( "AND G.CODEMP=? AND G.CODFILIAL=? AND G.CODGRUP LIKE ? " );
				sFiltros1.append( sFiltros1.length() > 0 ? " / " : "" );
				sFiltros1.append( "G.: " );
				sFiltros1.append( txtDescGrup1.getText().trim() );
			}
			if ( !sCodGrup2.equals( "" ) ) {
				sWhere.append( "AND ( NOT P.CODGRUP=? ) " );
				sFiltros1.append( sFiltros1.length() > 0 ? " / " : "" );
				sFiltros1.append( " EXCL. G.: " );
				sFiltros1.append( txtDescGrup2.getText().trim() );
			}
			if ( iCodSetor != 0 ) {
	
				if ( bPref ) {
					sWhere.append( "AND C1.CODSETOR=? " );
				}
				else {
					sWhere.append( "AND VD.CODSETOR=? " );
				}

//				sWhere.append( "AND VD.CODSETOR=? " );
				sFiltros2.append( sFiltros2.length() > 0 ? " / " : "" );
				sFiltros2.append( " SETOR: " );
				sFiltros2.append( iCodSetor );
				sFiltros2.append( "-" );
				sFiltros2.append( txtDescSetor.getVlrString().trim() );
			}
			if ( iCodVend != 0 ) {
				sWhere.append( "AND VD.CODVEND=? " );
				sFiltros2.append( sFiltros2.length() > 0 ? " / " : "" );
				sFiltros2.append( " REPR.: " );
				sFiltros2.append( iCodVend );
				sFiltros2.append( "-" );
				sFiltros2.append( txtNomeVend.getVlrString().trim() );
			}
			if ( iCodCli != 0 ) {
				if ( cbCliPrinc.getVlrString().equals( "S" ) ) {
					sFiltros2.append( sFiltros2.length() > 0 ? " / " : "" );
					sFiltros2.append( "AGRUP. CLI. PRINC." );
					sWhere.append( "AND C2.CODCLI=V.CODCLI AND C2.CODEMP=V.CODEMPCL AND C2.CODFILIAL=V.CODFILIALCL AND " );
					sWhere.append( "C2.CODPESQ=C1.CODPESQ AND C2.CODEMPPQ=C1.CODEMPPQ AND C2.CODFILIALPQ=C1.CODFILIALPQ AND C1.CODCLI=? " );
					sFrom = ", VDCLIENTE C1, VDCLIENTE C2 ";
				}
				else {
					sWhere.append( " and CODCLI=? " );
				}
				
				sFiltros2.append( sFiltros2.length() > 0 ? " / " : "" );
				sFiltros2.append( " CLI.: " );
				sFiltros2.append( iCodCli );
				sFiltros2.append( "-" );
				sFiltros2.append( Funcoes.copy( txtRazCli.getVlrString(), 30 ) );
			}
			else {
				sFrom = ",VDCLIENTE C1 ";
				sWhere.append( "AND C1.CODEMP=V.CODEMPCL AND C1.CODFILIAL=V.CODFILIALCL AND C1.CODCLI=V.CODCLI " );
			}

			if ( iCodTipoCli != 0 ) {

				if ( cbCliPrinc.getVlrString().equals( "S" ) ) {
					sWhere.append( "AND C2.CODCLI=V.CODCLI AND C2.CODEMP=V.CODEMPCL AND C2.CODFILIAL=V.CODFILIALCL AND " );
					sWhere.append( "C2.CODPESQ=C1.CODPESQ AND C2.CODEMPPQ=C1.CODEMPPQ AND " );
					sWhere.append( "C2.CODFILIALPQ=C1.CODFILIALPQ " );
					sFrom = ",VDCLIENTE C1, VDCLIENTE C2 ";
					sFiltros2.append( "AGRUP. CLI. PRINC." );
				}

				if ( sFrom.equals( "" ) ) {
					sWhere.append( "AND C1.CODCLI=V.CODCLI AND C1.CODEMP=V.CODEMPCL AND C1.CODFILIAL=V.CODFILIALCL " );
					sWhere.append( "AND C1.CODTIPOCLI=TC.CODTIPOCLI AND C1.CODEMPTI=TC.CODEMP AND C1.CODFILIALTI=TC.CODFILIAL " );
					sWhere.append( "AND TC.CODTIPOCLI=? " );
					sFrom = ", VDCLIENTE C1, VDTIPOCLI TC ";
				}
				else {
					sWhere.append( "AND C1.CODTIPOCLI=TC.CODTIPOCLI AND C1.CODEMPTI=TC.CODEMP AND C1.CODFILIALTI=TC.CODFILIAL " );
					sWhere.append( "AND TC.CODTIPOCLI=? " );
					sFrom = ", VDTIPOCLI TC " + sFrom;
				}
				sFiltros2.append( sFiltros2.length() > 0 ? " / " : "" );
				sFiltros2.append( " TP.CLI.: " );
				sFiltros2.append( iCodTipoCli );
				sFiltros2.append( "-" );
				sFiltros2.append( txtDescTipoCli.getVlrString().trim() );
			}

			if ( cbVendaCanc.getVlrString().equals( "N" ) ) {
				sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
			}

			if ( sFiltros1.length() > 0 ) {
				imp.addSubTitulo( sFiltros1.toString() );
			}
			if ( sFiltros2.length() > 0 ) {
				imp.addSubTitulo( sFiltros2.toString() );
			}
			if ( sCab.length() > 0 ) {
				imp.addSubTitulo( sCab );
			}

			sSQL.append( "SELECT ");
			
			if ( bPref ) {
				sSQL.append( "C1.CODSETOR, " );	
			}
			else {
				sSQL.append( "VD.CODSETOR, " );
			}
			
			sSQL.append( "CAST(SUBSTR(CAST(V.DTEMITVENDA AS CHAR(10)),1,7) AS CHAR(7)) ANO_MES," );
			sSQL.append( "G.CODGRUP,G.SIGLAGRUP,V.CODVEND,SUM(VLRLIQITVENDA) VLRVENDA " );
			sSQL.append( "FROM VDVENDA V, VDITVENDA IV, VDVENDEDOR VD, EQPRODUTO P, EQGRUPO G, EQTIPOMOV TM " );
			sSQL.append( sFrom );
			sSQL.append( "WHERE V.CODEMP=? AND V.CODFILIAL=? AND " + "V.DTEMITVENDA BETWEEN ? AND ? AND " );
			sSQL.append( "IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND " );
			sSQL.append( "IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA=V.TIPOVENDA AND " );
			sSQL.append( "VD.CODEMP=V.CODEMPVD AND VD.CODFILIAL=V.CODFILIALVD AND " );
			sSQL.append( "VD.CODVEND=V.CODVEND AND ");
					
			if ( bPref ) {
				sSQL.append( "C1.CODSETOR IS NOT NULL AND " );	
			}
			else {
				sSQL.append( "VD.CODSETOR IS NOT NULL AND " );
			}

			sSQL.append( "P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND " );
			sSQL.append( "P.CODPROD=IV.CODPROD AND G.CODEMP=P.CODEMPGP AND " );
			sSQL.append( "G.CODFILIAL=P.CODFILIALGP AND " );
			sSQL.append( "TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND " );
			sSQL.append( "TM.CODTIPOMOV=V.CODTIPOMOV " );
			sSQL.append( sWhereTM );
			sSQL.append( sWhere1 );
			sSQL.append( sWhere2 );
			sSQL.append( sWhere3 );
			sSQL.append( sWhere4 );
			sSQL.append( sCodGrup1.equals( "" ) ? " AND P.CODGRUP=G.CODGRUP " : " AND SUBSTR(P.CODGRUP,1," + sCodGrup1.length() + ")=G.CODGRUP " );
			sSQL.append( sWhere );
			sSQL.append( "GROUP BY 1,2,3,4,5" );
			sSQL.append( "ORDER BY 1,2,3,4,5" );

			System.out.println( sSQL.toString() );
			try {

				ps = con.prepareStatement( sSQL.toString() );
				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

				if ( !sCodMarca.equals( "" ) ) {
					ps.setInt( iParam++, Aplicativo.iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQMARCA" ) );
					ps.setString( iParam++, sCodMarca );
				}
				if ( !sCodGrup1.equals( "" ) ) {
					ps.setInt( iParam++, Aplicativo.iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQGRUPO" ) );
					ps.setString( iParam++, sCodGrup1 + ( sCodGrup1.length() < TAM_GRUPO ? "%" : "" ) );
				}
				if ( !sCodGrup2.equals( "" ) ) {
					ps.setString( iParam++, sCodGrup2 );
				}
				if ( iCodSetor != 0 ) {
					ps.setInt( iParam++, iCodSetor );
				}
				if ( iCodVend != 0 ) {
					ps.setInt( iParam++, iCodVend );
				}
				if ( iCodCli != 0 ) {
				
						ps.setInt( iParam++, iCodCli );
					
				}
				if ( iCodTipoCli != 0 ) {
					ps.setInt( iParam++, iCodTipoCli );
				}

				rs = ps.executeQuery();

				while ( rs.next() ) {

					vItem = new Vector<Object>();
					sCodSetor = rs.getString( 1 );
					sMes = rs.getString( 2 );
					sCodGrup = rs.getString( 3 );
					sSiglaGroup = rs.getString( 4 ) != null ? rs.getString( 4 ) : "";

					if ( !sCodSetorAnt.equals( "" ) && ( ( !sCodSetorAnt.equals( sCodSetor ) ) || ( !sCodGrupAnt.equals( sCodGrup ) ) || ( !sMesAnt.equals( sMes ) ) ) ) {
						deTotal1 = 0;
					}

					vItem.addElement( sCodSetor ); // 0 - Setor
					vItem.addElement( sMes ); // 1 - Mês
					vItem.addElement( sCodGrup ); // 2 - Cód. Grupo
					vItem.addElement( sSiglaGroup ); // 3 - Sigla Grupo
					vItem.addElement( String.valueOf( rs.getInt( 5 ) ) ); // 4 - Cód. Vendedor
					deValor = rs.getDouble( 6 );
					deTotal1 += deValor;
					deTotalGeral += deValor;
					vItem.addElement( new Double( deValor ) ); // 5 - Valor
					vItem.addElement( new Double( deTotal1 ) ); // 6 - Total do setor
					vItens.addElement( vItem );
					sCodSetorAnt = sCodSetor;
					sMesAnt = sMes;
					sCodGrupAnt = sCodGrup;
				}

				rs.close();
				ps.close();

				con.commit();

				vTotSetor = new Vector<Double>();

				sCodSetorAnt = "";
				sCodSetor = "";
				sMesAnt = "";
				sMes = "";
				sCodGrupAnt = "";
				sCodGrup = "";
				vCols = new Vector<String>();

				for ( int i = 0; i < vItens.size(); i++ ) {

					vItem = vItens.elementAt( i );

					if ( !sCodSetor.equals( vItem.elementAt( POS_CODSETOR ).toString() ) || !sMes.equals( vItem.elementAt( POS_MES ).toString() ) || !sCodGrup.equals( vItem.elementAt( POS_CODGRUP ).toString() ) ) {

						if ( !sCodSetorAnt.equals( "" ) ) {

							for ( int iConta = iPosCol; iConta < NUM_COLUNAS; iConta++ ) {
								imp.say( 21 + ( iConta * 11 ), "|" + StringFunctions.replicate( " ", 10 ) );
							}

							imp.say( 123, "|" + Funcoes.strDecimalToStrCurrency( 11, 2, vItens.elementAt( i - 1 ).elementAt( POS_TOTSETOR ).toString() ) );
							imp.say( 135, "|" );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
							iLinsSetor++;

							if ( imp.pRow() >= ( linPag - 1 ) ) {
								imp.pulaLinha( 1, imp.comprimido() );
								imp.incPags();
								imp.eject();
							}

						}

						if ( imp.pRow() == 0 ) {

							imp.impCab( 136, true );
							imp.say( 0, imp.comprimido() );
							imp.say( 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
							iCol = 0;
							iPosCol = 0;

						}

						if ( !sCodSetor.equals( vItem.elementAt( POS_CODSETOR ).toString() ) ) {

							if ( !sCodSetorAnt.equals( "" ) ) {

								if ( iLinsSetor > 1 ) {

									iTotPassadas = getPassadas( vCols.size() );

									for ( int iPassada = 0; iPassada < iTotPassadas; iPassada++ ) {
										imp.pulaLinha( 1, imp.comprimido() );
										imp.say( imp.pRow(), 0, ( iPassada == 0 ? "| SUBTOTAL" : "|        " ) );
										imp.say( imp.pRow(), 21, getTotSetor( vTotSetor, iTotPassadas, iPassada ) );
										imp.say( imp.pRow(), 135, "|" );
										imp.pulaLinha( 1, imp.comprimido() );
										imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
									}

								}

								iLinsSetor = 0;

							}

							sCodSetor = vItem.elementAt( POS_CODSETOR ).toString();
							vCols = getVendedores( sCodSetor, vItens );
							vTotSetor = initTotSetor( vCols );
							iTotPassadas = getPassadas( vCols.size() );

							for ( int iPassada = 0; iPassada < iTotPassadas; iPassada++ ) {

								if ( iPassada == 0 ) {
									imp.pulaLinha( 1, imp.comprimido() );
									imp.say( imp.pRow(), 0, "|" );
									imp.say( imp.pRow(), 70, "SETOR: " + sCodSetor );
									imp.say( imp.pRow(), 135, "|" );
								}

								imp.pulaLinha( 1, imp.comprimido() );

								if ( iPassada == 0 ) {
									imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
									imp.pulaLinha( 1, imp.comprimido() );
									imp.say( imp.pRow(), 0, "|MES" );
									imp.say( imp.pRow(), 9, "|GRUPO" );
								}
								else {
									imp.say( imp.pRow(), 0, "|" );
								}

								imp.say( imp.pRow(), 21, getColSetor( vCols, iTotPassadas, iPassada ) );
								imp.say( imp.pRow(), 135, "|" );

							}

							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
							iLinsSetor = 0;

						}

						sCodSetor = vItem.elementAt( POS_CODSETOR ).toString();
						sMes = vItem.elementAt( POS_MES ).toString();
						sCodGrup = vItem.elementAt( POS_CODGRUP ).toString();
						sCodSetorAnt = sCodSetor;
						sCodGrupAnt = sCodGrup;
						sMesAnt = sMes;
						iCol = 0;
						iPosCol = 0;

					}

					if ( iCol == 0 ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 0, "|" + vItem.elementAt( POS_MES ).toString() );
						imp.say( imp.pRow(), 9, "| " + ( vItem.elementAt( POS_SIGLAGRUP ).equals( "" ) ? Funcoes.copy( vItem.elementAt( POS_CODGRUP ).toString(), 10 ) : vItem.elementAt( POS_SIGLAGRUP ).toString() ) );
					}

					iPosColAnt = iPosCol;
					iCol = posVendedor( vItem.elementAt( POS_CODVEND ).toString(), vCols );
					iPosCol = getPosCol( iCol );

					if ( iCol >= NUM_COLUNAS && iPosCol < iPosColAnt ) { // Para fechar as colunas na linha atual

						for ( int iAjusta = iPosColAnt; iAjusta < NUM_COLUNAS; iAjusta++ ) {
							imp.say( imp.pRow(), 21 + ( iAjusta * 11 ), "|" + StringFunctions.replicate( " ", 10 ) );
						}

						iPosColAnt = 0;
						imp.say( imp.pRow(), 123, "|" );
						imp.say( imp.pRow(), 135, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 0, "|" );

					}

					for ( int iAjusta = iPosColAnt; iAjusta < iPosCol; iAjusta++ ) {
						imp.say( imp.pRow(), 21 + ( iAjusta * 11 ), "|" + StringFunctions.replicate( " ", 10 ) );
					}

					imp.say( imp.pRow(), 21 + ( iPosCol * 11 ), "|" + Funcoes.strDecimalToStrCurrency( 10, 2, vItem.elementAt( POS_VALOR ) + "" ) );

					vTotSetor = adicValorSetor( iCol, (Double) vItem.elementAt( POS_VALOR ), vTotSetor );
					iCol++;
					iPosCol++;
					iPos = i;

				}

				// Imprime o total do setor após a impressão do relatório, caso
				// não tenha sido impresso
				if ( iPos < vItens.size() ) {

					for ( int iConta = iCol; iConta < vCols.size(); iConta++ ) {

						iPosColAnt = iPosCol;
						iPosCol = getPosCol( iConta );

						if ( ( iConta >= NUM_COLUNAS ) && ( iPosCol < iPosColAnt ) ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( imp.pRow(), 0, "|" );
						}

						imp.say( imp.pRow(), 21 + ( iPosCol * 11 ), "|" + StringFunctions.replicate( " ", 10 ) );
						iPosCol++;

					}

					for ( int iConta = iPosCol; iConta < NUM_COLUNAS; iConta++ ) {
						imp.say( imp.pRow(), 21 + ( iConta * 11 ), "|" + StringFunctions.replicate( " ", 10 ) );
					}

					imp.say( imp.pRow(), 123, "|" + Funcoes.strDecimalToStrCurrency( 11, 2, vItens.elementAt( iPos ).elementAt( POS_TOTSETOR ).toString() ) );
					imp.say( imp.pRow(), 135, "|" );
					iLinsSetor++;
					iCol = 0;

				}

				if ( !sCodSetorAnt.equals( "" ) ) {

					if ( iLinsSetor > 1 ) {

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );

						iTotPassadas = getPassadas( vTotSetor.size() );

						for ( int iPassada = 0; iPassada < iTotPassadas; iPassada++ ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( imp.pRow(), 0, ( iPassada == 0 ? "| SUBTOTAL" : "|        " ) );
							imp.say( imp.pRow(), 21, getTotSetor( vTotSetor, iTotPassadas, iPassada ) );
							imp.say( imp.pRow(), 135, "|" );
						}

					}

					// Total Geral
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( imp.pRow(), 0, "| TOTAL" );
					imp.say( imp.pRow(), 123, "|" + Funcoes.strDecimalToStrCurrency( 11, 2, deTotalGeral + "" ) );
					imp.say( imp.pRow(), 135, "|" );

				}

				// Fim da impressão do total por setor

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "=", 133 ) + "+" );

				imp.eject();
				imp.fechaGravacao();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			sWhere = null;
			sSQL = null;
			sCodMarca = null;
			sCodSetor = null;
			sCodSetorAnt = null;
			sCodGrup1 = null;
			sCodGrup2 = null;
			sCodGrupAnt = null;
			sFiltros1 = null;
			sFiltros2 = null;
			sFrom = null;
			iCodSetor = 0;
			iCodVend = 0;
			iCodCli = 0;
			iTotPassadas = 0;
			iPosCol = 0;
			iPosColAnt = 0;
			sCodGrup = null;
			sSiglaGroup = null;
			sWhereTM = null;
			sMesAnt = null;
			sMes = null;
			imp = null;
			ps = null;
			rs = null;
			vItens = null;
			vItem = null;
			vCols = null;
			deTotal1 = 0;
			deTotalGeral = 0;
			deValor = 0;
			System.gc();
		}

	}

	private void impProduto( TYPE_PRINT bVisualizar, boolean postscript ) {

		if (postscript) {
			Funcoes.mensagemInforma( this, "Relatório gráfico não disponível para modo de impressão produto !" );
			return;
		}
		if ("D".equals(rgTipoDet.getVlrString() )) {
			Funcoes.mensagemInforma( this, "Relatório Detalhado não disponível para modo texto !" );
			return;
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sWhere4 = "";
		String sWhereTM = "";
		String sCab = "";
		String sFrom = "";
		String sCodMarca = null;
		String sCodGrup1 = null;
		String sCodGrup2 = null;
		StringBuffer sFiltros1 = new StringBuffer();
		StringBuffer sFiltros2 = new StringBuffer();
		;
		ImprimeOS imp = null;
		int iCodSetor = 0;
		int iCodVend = 0;
		int iCodCli = 0;
		int iCodTipoCli = 0;
		int linPag = 0;
		int iParam = 1;
		double deVlrTotal = 0;
		double deQtdTotal = 0;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatorio de Vendas por Setor x Produto" );
			imp.limpaPags();

			sCodMarca = txtCodMarca.getVlrString().trim();
			sCodGrup1 = txtCodGrup1.getVlrString().trim();
			sCodGrup2 = txtCodGrup2.getVlrString().trim();
			iCodSetor = txtCodSetor.getVlrInteger().intValue();
			iCodVend = txtCodVend.getVlrInteger().intValue();
			iCodCli = txtCodCli.getVlrInteger().intValue();
			iCodTipoCli = txtCodTipoCli.getVlrInteger().intValue();

			if ( rgFaturados.getVlrString().equals( "S" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab = "SO FATURADO";
			}
			else if ( rgFaturados.getVlrString().equals( "N" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				sCab = "NAO FATURADO";
			}
			else if ( rgFaturados.getVlrString().equals( "A" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}

			if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				sCab += sCab.length() > 0 ? " - SO FINANCEIRO" : "SO FINANCEIRO";
			}
			else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				sCab += sCab.length() > 0 ? " - NAO FINANCEIRO" : "NAO FINANCEIRO";
			}
			else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}

			if ( cbMovEstoque.getVlrString().equals( "S" ) ) {
				sFiltros1.append( "SO MOV.ESTOQUE" );
				sWhereTM = ( cbMovEstoque.getVlrString().equals( "S" ) ? " AND TM.ESTOQTIPOMOV='S' " : "" );
			}
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere4 = " AND V.STATUSVENDA IN ('V2','V3','P3') " ;
				sFiltros1.append( " EMITIDOS " );
			}

			if ( !sCodMarca.equals( "" ) ) {
				sWhere.append( "AND P.CODEMPMC=? AND P.CODFILIALMC=? AND P.CODMARCA=? " );
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( "M.: " );
				sFiltros1.append( txtDescMarca.getText().trim() );
			}
			if ( !sCodGrup1.equals( "" ) ) {
				sWhere.append( "AND G.CODEMP=? AND G.CODFILIAL=? AND G.CODGRUP LIKE ? " );
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( "G.: " );
				sFiltros1.append( txtDescGrup1.getText().trim() );
			}
			if ( !sCodGrup2.equals( "" ) ) {
				sWhere.append( "AND ( NOT P.CODGRUP=? ) " );
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( " EXCL. G.: " );
				sFiltros1.append( txtDescGrup2.getText().trim() );
			}
			if ( iCodSetor != 0 ) {
				
				if ( bPref ) {
					sWhere.append( "AND C1.CODSETOR=? " );
				}
				else {
					sWhere.append( "AND VD.CODSETOR=? " );
				}
				
				sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
				sFiltros2.append( " SETOR: " );
				sFiltros2.append( iCodSetor );
				sFiltros2.append( "-" );
				sFiltros2.append( txtDescSetor.getVlrString().trim() );
			}
			if ( iCodVend != 0 ) {
				sWhere.append( "AND V.CODVEND=? " );
				sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
				sFiltros2.append( " REPR.: " );
				sFiltros2.append( iCodVend );
				sFiltros2.append( "-" );
				sFiltros2.append( txtNomeVend.getVlrString().trim() );
			}
			if ( iCodCli != 0 ) {
				if ( iCodCli != 0 ) {
					if ( cbCliPrinc.getVlrString().equals( "S" ) ) {
						sWhere.append( "AND C2.CODCLI=V.CODCLI AND C2.CODEMP=V.CODEMPCL AND C2.CODFILIAL=V.CODFILIALCL AND " );
						sWhere.append( "C2.CODPESQ=C1.CODPESQ AND C2.CODEMPPQ=C1.CODEMPPQ AND " );
						sWhere.append( "C2.CODFILIALPQ=C1.CODFILIALPQ AND C1.CODCLI=? " );
						sFrom = ",VDCLIENTE C1, VDCLIENTE C2 ";
						sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
						sFiltros2.append( "AGRUP. CLI. PRINC." );
					}
					else {
						sWhere.append( "AND V.CODCLI=? " );
					}
					sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
					sFiltros2.append( " CLI.: " );
					sFiltros2.append( iCodCli );
					sFiltros2.append( "-" );
					sFiltros2.append( Funcoes.copy( txtRazCli.getVlrString(), 30 ) );
				}
			}
			else {
				sFrom = ",VDCLIENTE C1 ";
				sWhere.append( "AND C1.CODEMP=V.CODEMPCL AND C1.CODFILIAL=V.CODFILIALCL AND C1.CODCLI=V.CODCLI " );
			}
			if ( iCodTipoCli != 0 ) {
				if ( cbCliPrinc.getVlrString().equals( "S" ) ) {
					sWhere.append( "AND C2.CODCLI=V.CODCLI AND C2.CODEMP=V.CODEMPCL AND C2.CODFILIAL=V.CODFILIALCL AND " );
					sWhere.append( "C2.CODPESQ=C1.CODPESQ AND C2.CODEMPPQ=C1.CODEMPPQ AND " );
					sWhere.append( "C2.CODFILIALPQ=C1.CODFILIALPQ " );
					sFrom = ",VDCLIENTE C1, VDCLIENTE C2 ";
					sFiltros2.append( "AGRUP. CLI. PRINC." );
				}
				else {
					if ( sFrom.equals( "" ) ) {
						sWhere.append( "AND C1.CODCLI=V.CODCLI AND C1.CODEMP=V.CODEMPCL AND C1.CODFILIAL=V.CODFILIALCL " );
						sWhere.append( "AND C1.CODTIPOCLI=TC.CODTIPOCLI AND C1.CODEMPTI=TC.CODEMP AND C1.CODFILIALTI=TC.CODFILIAL " );
						sWhere.append( "AND TC.CODTIPOCLI=? " );
						sFrom = ",VDCLIENTE C1, VDTIPOCLI TC ";
					}
					else {
						sWhere.append( "AND C1.CODTIPOCLI=TC.CODTIPOCLI AND C1.CODEMPTI=TC.CODEMP AND C1.CODFILIALTI=TC.CODFILIAL " );
						sWhere.append( "AND TC.CODTIPOCLI=? " );
						sFrom = ", VDTIPOCLI TC " + sFrom;
					}
				}
				sFiltros2.append( sFiltros2.length() > 0 ? " / " : "" );
				sFiltros2.append( " TP.CLI.: " );
				sFiltros2.append( iCodTipoCli );
				sFiltros2.append( "-" );
				sFiltros2.append( txtDescTipoCli.getVlrString().trim() );
			}

			if ( txtCodCFOP.getVlrInteger().intValue() > 0 ) {
				sWhere1 += " AND IV.CODNAT='" + txtCodCFOP.getVlrString()+"'";
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( " CFOP.: " );
				sFiltros1.append( txtDescCFOP.getText().trim() );
			}
			if ( txtCodTipoMov.getVlrInteger().intValue() > 0 ) {
				sWhere1 += " AND V.CODTIPOMOV=" + txtCodTipoMov.getVlrInteger().intValue();
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( " TipoMov.: " );
				sFiltros1.append( txtDescTipoMov.getText().trim() );
			}
			
			if ( cbVendaCanc.getVlrString().equals( "N" ) ) {
				sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
			}

			if ( sFiltros1.length() > 0 ) {
				imp.addSubTitulo( sFiltros1.toString() );
			}
			if ( sFiltros2.length() > 0 ) {
				imp.addSubTitulo( sFiltros2.toString() );
			}
			if ( sCab.length() > 0 ) {
				imp.addSubTitulo( sCab );
			}

			try {

				sSql.append( "SELECT P.DESCPROD,P.CODPROD,P.REFPROD,SUM(IV.QTDITVENDA) QTDVENDA ,SUM(IV.VLRLIQITVENDA) VLRVENDA " );
				sSql.append( "FROM VDVENDA V, VDITVENDA IV, VDVENDEDOR VD, EQPRODUTO P, EQGRUPO G, EQTIPOMOV TM " );
				sSql.append( sFrom );
				sSql.append( "WHERE V.CODEMP=? AND V.CODFILIAL=? " );
				sSql.append( "AND V.DTEMITVENDA BETWEEN ? AND ? " );
				sSql.append( "AND IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND " );
				sSql.append( "IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA=V.TIPOVENDA AND " );
				sSql.append( "VD.CODEMP=V.CODEMPVD AND VD.CODFILIAL=V.CODFILIALVD AND " );
				
				sSql.append( "VD.CODVEND=V.CODVEND AND ");

				if ( bPref ) {
					sSql.append( "C1.CODSETOR IS NOT NULL AND " );	
				}
				else {
					sSql.append( "VD.CODSETOR IS NOT NULL AND " );
				}

				sSql.append( "P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND " );
				sSql.append( "P.CODPROD=IV.CODPROD AND G.CODEMP=P.CODEMPGP AND " );
				sSql.append( "G.CODFILIAL=P.CODFILIALGP AND " );
				sSql.append( "TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND " );
				sSql.append( "TM.CODTIPOMOV=V.CODTIPOMOV " );
				sSql.append( sWhereTM );
				sSql.append( sWhere1 );
				sSql.append( sWhere2 );
				sSql.append( sWhere3 );
				sSql.append( sWhere4 );
				sSql.append( sCodGrup1.equals( "" ) ? " AND P.CODGRUP=G.CODGRUP " : " AND SUBSTR(P.CODGRUP,1," + sCodGrup1.length() + ")=G.CODGRUP " );
				sSql.append( sWhere );
				sSql.append( "GROUP BY 1,2,3" );
				sSql.append( "ORDER BY 1,2,3" );

				ps = con.prepareStatement( sSql.toString() );
				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				if ( !sCodMarca.equals( "" ) ) {
					ps.setInt( iParam++, Aplicativo.iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQMARCA" ) );
					ps.setString( iParam++, sCodMarca );
				}
				if ( !sCodGrup1.equals( "" ) ) {
					ps.setInt( iParam++, Aplicativo.iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQGRUPO" ) );
					ps.setString( iParam++, sCodGrup1 + ( sCodGrup1.length() < TAM_GRUPO ? "%" : "" ) );
				}
				if ( !sCodGrup2.equals( "" ) ) {
					ps.setString( iParam++, sCodGrup2 );
				}
				if ( iCodSetor != 0 ) {
					ps.setInt( iParam++, iCodSetor );
				}
				if ( iCodVend != 0 ) {
					ps.setInt( iParam++, iCodVend );
				}
				if ( iCodCli != 0 ) {
					ps.setInt( iParam++, iCodCli );
				}
				if ( iCodTipoCli != 0 ) {
					ps.setInt( iParam++, iCodTipoCli );
				}

				rs = ps.executeQuery();

				while ( rs.next() ) {

					if ( imp.pRow() >= ( linPag - 1 ) ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.incPags();
						imp.eject();
					}

					if ( imp.pRow() == 0 ) {

						imp.impCab( 136, true );

						imp.say( 0, imp.comprimido() );
						imp.say( 1, "|" );
						imp.say( 50, "PERIODO DE: " + txtDataini.getVlrString() + " ATE: " + txtDatafim.getVlrString() );
						imp.say( 136, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "| DESCRICAO DO PRODUTO" );
						imp.say( 55, "| CODIGO" );
						imp.say( 67, "| QUANTIDADE" );
						imp.say( 81, "|     VALOR" );
						imp.say( 99, "|" );
						imp.say( 136, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

					}

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 1, "|" );
					imp.say( 4, Funcoes.adicionaEspacos( rs.getString( 1 ), 50 ) + " |" );
					imp.say( 56, Funcoes.adicEspacosEsquerda( rs.getString( 2 ), 10 ) + " |" );
					imp.say( 70, Funcoes.adicEspacosEsquerda( String.valueOf( rs.getDouble( 4 ) ), 10 ) + " |" );
					imp.say( 83, Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 5 ) ) + " |" );
					imp.say( 136, "|" );

					deQtdTotal += rs.getDouble( 4 );
					deVlrTotal += rs.getDouble( 5 );

				}

				// Fim da impressão do total por setor

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "| TOTAL" );
				imp.say( 67, "| " + Funcoes.strDecimalToStrCurrency( 11, 2, String.valueOf( deQtdTotal ) ) );
				imp.say( 81, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( deVlrTotal ) ) + " |" );
				imp.say( 136, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

				imp.eject();
				imp.fechaGravacao();

				rs.close();
				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSql = null;
			sWhere = null;
			sWhere1 = null;
			sWhere2 = null;
			sWhere3 = null;
			sWhereTM = null;
			sCab = null;
			sFrom = null;
			sCodMarca = null;
			sCodGrup1 = null;
			sCodGrup2 = null;
			sFiltros1 = null;
			sFiltros2 = null;
			imp = null;
			System.gc();
		}

	}
	
	private void impConserto( TYPE_PRINT bVisualizar, boolean postscript ) {

		if (postscript) {
			Funcoes.mensagemInforma( this, "Relatório gráfico não disponível para modo de impressão produto !" );
			return;
		}
		if ("D".equals(rgTipoDet.getVlrString() )) {
			Funcoes.mensagemInforma( this, "Relatório Detalhado não disponível para modo texto !" );
			return;
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sWhere4 = "";
		String sWhereTM = "";
		String sCab = "";
		String sFrom = "";
		String sCodMarca = null;
		String sCodGrup1 = null;
		String sCodGrup2 = null;
		StringBuffer sFiltros1 = new StringBuffer();
		StringBuffer sFiltros2 = new StringBuffer();
		;
		ImprimeOS imp = null;
		int iCodSetor = 0;
		int iCodVend = 0;
		int iCodCli = 0;
		int iCodTipoCli = 0;
		int linPag = 0;
		int iParam = 1;
		double deVlrTotal = 0;
		double deQtdTotal = 0;

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatorio de Vendas por Setor x Produto" );
			imp.limpaPags();

			sCodMarca = txtCodMarca.getVlrString().trim();
			sCodGrup1 = txtCodGrup1.getVlrString().trim();
			sCodGrup2 = txtCodGrup2.getVlrString().trim();
			iCodSetor = txtCodSetor.getVlrInteger().intValue();
			iCodVend = txtCodVend.getVlrInteger().intValue();
			iCodCli = txtCodCli.getVlrInteger().intValue();
			iCodTipoCli = txtCodTipoCli.getVlrInteger().intValue();

			if ( rgFaturados.getVlrString().equals( "S" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
				sCab = "SO FATURADO";
			}
			else if ( rgFaturados.getVlrString().equals( "N" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
				sCab = "NAO FATURADO";
			}
			else if ( rgFaturados.getVlrString().equals( "A" ) ) {
				sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
			}
			
			if ( txtCodCFOP.getVlrInteger().intValue() > 0 ) {
				sWhere1 += " AND IV.CODNAT=" + txtCodCFOP.getVlrInteger().intValue();
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( " CFOP.: " );
				sFiltros1.append( txtDescCFOP.getText().trim() );
			}
			if ( txtCodTipoMov.getVlrInteger().intValue() > 0 ) {
				sWhere1 += " AND V.CODTIPOMOV=" + txtCodTipoMov.getVlrInteger().intValue();
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( " TipoMov.: " );
				sFiltros1.append( txtDescTipoMov.getText().trim() );
			}


			if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
				sCab += sCab.length() > 0 ? " - SO FINANCEIRO" : "SO FINANCEIRO";
			}
			else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
				sCab += sCab.length() > 0 ? " - NAO FINANCEIRO" : "NAO FINANCEIRO";
			}
			else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
				sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
			}

			if ( cbMovEstoque.getVlrString().equals( "S" ) ) {
				sFiltros1.append( "SO MOV.ESTOQUE" );
				sWhereTM = ( cbMovEstoque.getVlrString().equals( "S" ) ? " AND TM.ESTOQTIPOMOV='S' " : "" );
			}
			
			if ( rgEmitidos.getVlrString().equals( "S" ) ) {
				sWhere4 = " AND V.STATUSVENDA IN ('V2','V3','P3') " ;
				sFiltros1.append( " EMITIDOS " );
			}

			if ( !sCodMarca.equals( "" ) ) {
				sWhere.append( "AND P.CODEMPMC=? AND P.CODFILIALMC=? AND P.CODMARCA=? " );
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( "M.: " );
				sFiltros1.append( txtDescMarca.getText().trim() );
			}
			if ( !sCodGrup1.equals( "" ) ) {
				sWhere.append( "AND G.CODEMP=? AND G.CODFILIAL=? AND G.CODGRUP LIKE ? " );
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( "G.: " );
				sFiltros1.append( txtDescGrup1.getText().trim() );
			}
			if ( !sCodGrup2.equals( "" ) ) {
				sWhere.append( "AND ( NOT P.CODGRUP=? ) " );
				sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
				sFiltros1.append( " EXCL. G.: " );
				sFiltros1.append( txtDescGrup2.getText().trim() );
			}
			if ( iCodSetor != 0 ) {
				
				if ( bPref ) {
					sWhere.append( "AND C1.CODSETOR=? " );
				}
				else {
					sWhere.append( "AND VD.CODSETOR=? " );
				}
				
				sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
				sFiltros2.append( " SETOR: " );
				sFiltros2.append( iCodSetor );
				sFiltros2.append( "-" );
				sFiltros2.append( txtDescSetor.getVlrString().trim() );
			}
			if ( iCodVend != 0 ) {
				sWhere.append( "AND V.CODVEND=? " );
				sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
				sFiltros2.append( " REPR.: " );
				sFiltros2.append( iCodVend );
				sFiltros2.append( "-" );
				sFiltros2.append( txtNomeVend.getVlrString().trim() );
			}
			if ( iCodCli != 0 ) {
				if ( iCodCli != 0 ) {
					if ( cbCliPrinc.getVlrString().equals( "S" ) ) {
						sWhere.append( "AND C2.CODCLI=V.CODCLI AND C2.CODEMP=V.CODEMPCL AND C2.CODFILIAL=V.CODFILIALCL AND " );
						sWhere.append( "C2.CODPESQ=C1.CODPESQ AND C2.CODEMPPQ=C1.CODEMPPQ AND " );
						sWhere.append( "C2.CODFILIALPQ=C1.CODFILIALPQ AND C1.CODCLI=? " );
						sFrom = ",VDCLIENTE C1, VDCLIENTE C2 ";
						sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
						sFiltros2.append( "AGRUP. CLI. PRINC." );
					}
					else {
						sWhere.append( "AND V.CODCLI=? " );
					}
					sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
					sFiltros2.append( " CLI.: " );
					sFiltros2.append( iCodCli );
					sFiltros2.append( "-" );
					sFiltros2.append( Funcoes.copy( txtRazCli.getVlrString(), 30 ) );
				}
			}
			else {
				sFrom = ",VDCLIENTE C1 ";
				sWhere.append( "AND C1.CODEMP=V.CODEMPCL AND C1.CODFILIAL=V.CODFILIALCL AND C1.CODCLI=V.CODCLI " );
			}
			if ( iCodTipoCli != 0 ) {
				if ( cbCliPrinc.getVlrString().equals( "S" ) ) {
					sWhere.append( "AND C2.CODCLI=V.CODCLI AND C2.CODEMP=V.CODEMPCL AND C2.CODFILIAL=V.CODFILIALCL AND " );
					sWhere.append( "C2.CODPESQ=C1.CODPESQ AND C2.CODEMPPQ=C1.CODEMPPQ AND " );
					sWhere.append( "C2.CODFILIALPQ=C1.CODFILIALPQ " );
					sFrom = ",VDCLIENTE C1, VDCLIENTE C2 ";
					sFiltros2.append( "AGRUP. CLI. PRINC." );
				}
				else {
					if ( sFrom.equals( "" ) ) {
						sWhere.append( "AND C1.CODCLI=V.CODCLI AND C1.CODEMP=V.CODEMPCL AND C1.CODFILIAL=V.CODFILIALCL " );
						sWhere.append( "AND C1.CODTIPOCLI=TC.CODTIPOCLI AND C1.CODEMPTI=TC.CODEMP AND C1.CODFILIALTI=TC.CODFILIAL " );
						sWhere.append( "AND TC.CODTIPOCLI=? " );
						sFrom = ",VDCLIENTE C1, VDTIPOCLI TC ";
					}
					else {
						sWhere.append( "AND C1.CODTIPOCLI=TC.CODTIPOCLI AND C1.CODEMPTI=TC.CODEMP AND C1.CODFILIALTI=TC.CODFILIAL " );
						sWhere.append( "AND TC.CODTIPOCLI=? " );
						sFrom = ", VDTIPOCLI TC " + sFrom;
					}
				}
				sFiltros2.append( sFiltros2.length() > 0 ? " / " : "" );
				sFiltros2.append( " TP.CLI.: " );
				sFiltros2.append( iCodTipoCli );
				sFiltros2.append( "-" );
				sFiltros2.append( txtDescTipoCli.getVlrString().trim() );
			}

			if ( cbVendaCanc.getVlrString().equals( "N" ) ) {
				sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
			}

			if ( sFiltros1.length() > 0 ) {
				imp.addSubTitulo( sFiltros1.toString() );
			}
			if ( sFiltros2.length() > 0 ) {
				imp.addSubTitulo( sFiltros2.toString() );
			}
			if ( sCab.length() > 0 ) {
				imp.addSubTitulo( sCab );
			}

			try {

				sSql.append( "SELECT coalesce(pd2.descprod,pd1.descprod) descprod,coalesce(pd2.codprod,pd1.codprod) codprod, coalesce(Pd2.REFPROD, pd1.refprod) refprod,SUM(IV.QTDITVENDA) QTDVENDA ,SUM(IV.VLRLIQITVENDA) VLRVENDA " );
				
				sSql.append( "FROM VDVENDEDOR VD " );
				sSql.append( sFrom );
				sSql.append( " ,eqtipomov tm, vdvenda v, vditvenda iv "); 
				sSql.append( "left outer join eqproduto pd1 on ");
				sSql.append( "pd1.codemp=iv.codemppd and pd1.codfilial=iv.codfilialpd and pd1.codprod=iv.codprod ");
				sSql.append( "left outer join eqgrupo g1 on ");
				sSql.append( "g1.codemp=pd1.codempgp and g1.codfilial=pd1.codfilialgp and g1.codgrup=pd1.codgrup ");
				sSql.append( "left outer join vdvendaorc vo on ");
				sSql.append( "vo.codemp=iv.codemp and vo.codfilial=iv.codfilial and vo.codvenda=iv.codvenda and vo.tipovenda=iv.tipovenda and vo.coditvenda=iv.coditvenda ");
				sSql.append( "left outer join eqitrecmercitositorc iro on ");
				sSql.append( "iro.codempoc=vo.codempor and iro.codfilialoc=vo.codfilialor and iro.codorc=vo.codorc and iro.tipoorc=vo.tipoorc and iro.coditorc=vo.coditorc ");
				sSql.append( "left outer join eqitrecmerc ir on ");
				sSql.append( "ir.codemp=iro.codemp and ir.codfilial=iro.codfilial and ir.ticket=iro.ticket and ir.coditrecmerc=iro.coditrecmerc ");
				sSql.append( "left outer join eqproduto pd2 on ");
				sSql.append( "pd2.codemp=ir.codemppd and pd2.codfilial=ir.codfilialpd and pd2.codprod=ir.codprod ");
				sSql.append( "left outer join eqgrupo g2 on ");
				sSql.append( "g2.codemp=pd2.codempgp and g2.codfilial=pd2.codfilialgp and g2.codgrup=pd2.codgrup " );
				
				sSql.append( "WHERE V.CODEMP=? AND V.CODFILIAL=? " );
				sSql.append( "AND V.DTEMITVENDA BETWEEN ? AND ? " );
				sSql.append( "AND IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND " );
				sSql.append( "IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA=V.TIPOVENDA AND " );
				sSql.append( "VD.CODEMP=V.CODEMPVD AND VD.CODFILIAL=V.CODFILIALVD AND " );
				
				sSql.append( "VD.CODVEND=V.CODVEND AND ");

				if ( bPref ) {
					sSql.append( "C1.CODSETOR IS NOT NULL AND " );	
				}
				else {
					sSql.append( "VD.CODSETOR IS NOT NULL AND " );
				}

//				sSql.append( "P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND " );
//				sSql.append( "P.CODPROD=IV.CODPROD AND G.CODEMP=P.CODEMPGP AND " );
	//			sSql.append( "G.CODFILIAL=P.CODFILIALGP AND " );
				sSql.append( "TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND " );
				sSql.append( "TM.CODTIPOMOV=V.CODTIPOMOV " );

				sSql.append( sWhereTM );
				sSql.append( sWhere1 );
				sSql.append( sWhere2 );
				sSql.append( sWhere3 );
				sSql.append( sWhere4 );
				
				sSql.append( sCodGrup1.equals( "" ) ? " AND COALESCE(PD2.CODGRUP,PD1.CODGRUP)=COALESCE(G2.CODGRUP,G1.CODGRUP) " : " AND SUBSTR(COALESCE(PD2.CODGRUP,PD1.CODGRUP),1," + sCodGrup1.length() + ")=COALESCE(G2.CODGRUP,G1.CODGRUP) " );
				
				sSql.append( sWhere );
				
				sSql.append( " GROUP BY 1,2,3" );
				sSql.append( " ORDER BY 1,2,3" );

				System.out.println("SQL:" + sSql.toString());
				
				ps = con.prepareStatement( sSql.toString() );
				
				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
				ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
				
				if ( !sCodMarca.equals( "" ) ) {
					ps.setInt( iParam++, Aplicativo.iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQMARCA" ) );
					ps.setString( iParam++, sCodMarca );
				}
				if ( !sCodGrup1.equals( "" ) ) {
					ps.setInt( iParam++, Aplicativo.iCodEmp );
					ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQGRUPO" ) );
					ps.setString( iParam++, sCodGrup1 + ( sCodGrup1.length() < TAM_GRUPO ? "%" : "" ) );
				}
				if ( !sCodGrup2.equals( "" ) ) {
					ps.setString( iParam++, sCodGrup2 );
				}
				if ( iCodSetor != 0 ) {
					ps.setInt( iParam++, iCodSetor );
				}
				if ( iCodVend != 0 ) {
					ps.setInt( iParam++, iCodVend );
				}
				if ( iCodCli != 0 ) {
					ps.setInt( iParam++, iCodCli );
				}
				if ( iCodTipoCli != 0 ) {
					ps.setInt( iParam++, iCodTipoCli );
				}

				
				
				rs = ps.executeQuery();

				while ( rs.next() ) {

					if ( imp.pRow() >= ( linPag - 1 ) ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.incPags();
						imp.eject();
					}

					if ( imp.pRow() == 0 ) {

						imp.impCab( 136, true );

						imp.say( 0, imp.comprimido() );
						imp.say( 1, "|" );
						imp.say( 50, "PERIODO DE: " + txtDataini.getVlrString() + " ATE: " + txtDatafim.getVlrString() );
						imp.say( 136, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "| DESCRICAO DO PRODUTO" );
						imp.say( 55, "| CODIGO" );
						imp.say( 67, "| QUANTIDADE" );
						imp.say( 81, "|     VALOR" );
						imp.say( 99, "|" );
						imp.say( 136, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

					}

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 1, "|" );
					imp.say( 4, Funcoes.adicionaEspacos( rs.getString( 1 ), 50 ) + " |" );
					imp.say( 56, Funcoes.adicEspacosEsquerda( rs.getString( 2 ), 10 ) + " |" );
					imp.say( 70, Funcoes.adicEspacosEsquerda( String.valueOf( rs.getDouble( 4 ) ), 10 ) + " |" );
					imp.say( 83, Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 5 ) ) + " |" );
					imp.say( 136, "|" );

					deQtdTotal += rs.getDouble( 4 );
					deVlrTotal += rs.getDouble( 5 );

				}

				// Fim da impressão do total por setor

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "| TOTAL" );
				imp.say( 67, "| " + Funcoes.strDecimalToStrCurrency( 11, 2, String.valueOf( deQtdTotal ) ) );
				imp.say( 81, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( deVlrTotal ) ) + " |" );
				imp.say( 136, "|" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

				imp.eject();
				imp.fechaGravacao();

				rs.close();
				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSql = null;
			sWhere = null;
			sWhere1 = null;
			sWhere2 = null;
			sWhere3 = null;
			sWhereTM = null;
			sCab = null;
			sFrom = null;
			sCodMarca = null;
			sCodGrup1 = null;
			sCodGrup2 = null;
			sFiltros1 = null;
			sFiltros2 = null;
			imp = null;
			System.gc();
		}

	}

	private boolean getPrefere() {

		boolean retorno = false;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT SETORVENDA FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				retorno = rs.getString( "SETORVENDA" ) != null && "CA".indexOf( rs.getString( "SETORVENDA" ) ) >= 0;
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao verificar preferências!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}

		return retorno;
	}
	
	private void impCliente(TYPE_PRINT bVisualizar, boolean postscript, boolean detalhado, Blob fotoemp) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSql = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		String sWhere1 = "";
		String sWhere2 = "";
		String sWhere3 = "";
		String sWhere4 = "";
		String sWhereTM = "";
		String sCab = "";
		String sFrom = "";
		String sCodMarca = null;
		String sCodGrup1 = null;
		String sCodGrup2 = null;
		String sOrdemRel = null;
		String sOrderBy = null;
		String sDescOrdemRel = null;
		StringBuffer sFiltros1 = new StringBuffer();
		StringBuffer sFiltros2 = new StringBuffer();

		int iCodSetor = 0;
		int iCodCli = 0;
		int iCodTipoCli = 0;
		int iCodVend = 0;
		int iCodTipoMov = 0;
		int iParam = 1;
		
		sCodMarca = txtCodMarca.getVlrString().trim();
		sCodGrup1 = txtCodGrup1.getVlrString().trim();
		sCodGrup2 = txtCodGrup2.getVlrString().trim();
		iCodSetor = txtCodSetor.getVlrInteger().intValue();
		iCodVend = txtCodVend.getVlrInteger().intValue();
		iCodCli = txtCodCli.getVlrInteger().intValue();
		iCodTipoCli = txtCodTipoCli.getVlrInteger().intValue();
		iCodTipoMov = txtCodTipoMov.getVlrInteger();
		sOrdemRel = rgOrdemRel.getVlrString();

		if ( rgFaturados.getVlrString().equals( "S" ) ) {
			sWhere1 = " AND TM.FISCALTIPOMOV='S' ";
			sCab = "SO FATURADO";
		}
		else if ( rgFaturados.getVlrString().equals( "N" ) ) {
			sWhere1 = " AND TM.FISCALTIPOMOV='N' ";
			sCab = "NAO FATURADO";
		}
		else if ( rgFaturados.getVlrString().equals( "A" ) ) {
			sWhere1 = " AND TM.FISCALTIPOMOV IN ('S','N') ";
		}

		if ( rgFinanceiro.getVlrString().equals( "S" ) ) {
			sWhere2 = " AND TM.SOMAVDTIPOMOV='S' ";
			sCab += sCab.length() > 0 ? " - SO FINANCEIRO" : "SO FINANCEIRO";
		}
		else if ( rgFinanceiro.getVlrString().equals( "N" ) ) {
			sWhere2 = " AND TM.SOMAVDTIPOMOV='N' ";
			sCab += sCab.length() > 0 ? " - NAO FINANCEIRO" : "NAO FINANCEIRO";
		}
		else if ( rgFinanceiro.getVlrString().equals( "A" ) ) {
			sWhere2 = " AND TM.SOMAVDTIPOMOV IN ('S','N') ";
		}
		
		if ( rgEmitidos.getVlrString().equals( "S" ) ) {
			sWhere4 = " AND V.STATUSVENDA IN ('V2','V3','P3') " ;
			sFiltros1.append( " EMITIDOS " );
		}

		if ( cbMovEstoque.getVlrString().equals( "S" ) ) {
			sFiltros1.append( "SO MOV.ESTOQUE" );
			sWhereTM = ( cbMovEstoque.getVlrString().equals( "S" ) ? " AND TM.ESTOQTIPOMOV='S' " : "" );
		}

		if ( !sCodMarca.equals( "" ) ) {
			sWhere.append( "AND P.CODEMPMC=? AND P.CODFILIALMC=? AND P.CODMARCA=? " );
			sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
			sFiltros1.append( "M.: " );
			sFiltros1.append( txtDescMarca.getText().trim() );
		}
		if ( !sCodGrup1.equals( "" ) ) {
			sWhere.append( "AND G.CODEMP=? AND G.CODFILIAL=? AND G.CODGRUP LIKE ? " );
			sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
			sFiltros1.append( "G.: " );
			sFiltros1.append( txtDescGrup1.getText().trim() );
		}
		if ( !sCodGrup2.equals( "" ) ) {
			sWhere.append( "AND ( NOT P.CODGRUP=? ) " );
			sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
			sFiltros1.append( " EXCL. G.: " );
			sFiltros1.append( txtDescGrup2.getText().trim() );
		}
		if ( txtCodCFOP.getVlrInteger().intValue() > 0 ) {
			sWhere1 += " AND IV.CODNAT=" + txtCodCFOP.getVlrInteger().intValue();
			sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
			sFiltros1.append( " CFOP.: " );
			sFiltros1.append( txtDescCFOP.getText().trim() );
		}
		if ( txtCodTipoMov.getVlrInteger().intValue() > 0 ) {
			sWhere1 += " AND V.CODTIPOMOV=" + txtCodTipoMov.getVlrInteger().intValue();
			sFiltros1.append( !sFiltros1.equals( "" ) ? " / " : "" );
			sFiltros1.append( " TipoMov.: " );
			sFiltros1.append( txtDescTipoMov.getText().trim() );
		}

		
		if ( iCodSetor != 0 ) {
			
			if ( bPref ) {
				sWhere.append( "AND C.CODSETOR=? " );
			}
			else {
				sWhere.append( "AND VD.CODSETOR=? " );
			}
			
			sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
			sFiltros2.append( " SETOR: " );
			sFiltros2.append( iCodSetor );
			sFiltros2.append( "-" );
			sFiltros2.append( txtDescSetor.getVlrString().trim() );
		}
		if ( iCodVend != 0 ) {
			sWhere.append( "AND V.CODVEND=? " );
			sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
			sFiltros2.append( " REPR.: " );
			sFiltros2.append( iCodVend );
			sFiltros2.append( "-" );
			sFiltros2.append( txtNomeVend.getVlrString().trim() );
		}
		if ( iCodCli != 0 ) {
			sWhere.append( "AND C2.CODCLI=? " );
			sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
			sFiltros2.append( " CLI.: " );
			sFiltros2.append( iCodCli );
			sFiltros2.append( "-" );
			sFiltros2.append( Funcoes.copy( txtRazCli.getVlrString(), 30 ) );
		}
		if ( iCodTipoCli != 0 ) {
			sWhere.append( "AND C2.CODTIPOCLI=TC.CODTIPOCLI AND C2.CODEMPTI=TC.CODEMP AND C2.CODFILIALTI=TC.CODFILIAL " );
			sWhere.append( "AND TC.CODTIPOCLI=? " );
			sFrom = ", VDTIPOCLI TC ";
			sFiltros2.append( sFiltros2.length() > 0 ? " / " : "" );
			sFiltros2.append( " TP.CLI.: " );
			sFiltros2.append( iCodTipoCli );
			sFiltros2.append( "-" );
			sFiltros2.append( txtDescTipoCli.getVlrString().trim() );
		}

		if ( cbVendaCanc.getVlrString().equals( "N" ) ) {
			sWhere3 = " AND NOT SUBSTR(V.STATUSVENDA,1,1)='C' ";
		}

		if ( cbCliPrinc.getVlrString().equals( "S" ) ) {
			sFiltros2.append( !sFiltros2.equals( "" ) ? " / " : "" );
			sFiltros2.append( "ADIC. CLIENTES PRINCIPAIS" );
		}

		if ( sOrdemRel.equals( "V" ) ) {
			sOrderBy = "1,2,6 desc";
			sDescOrdemRel = "Valor";
		}
		else if ( sOrdemRel.equals( "R" ) ) {
			sOrderBy = "1,2,3,4";
			sDescOrdemRel = "Razão social";
		}
		else if ( sOrdemRel.equals( "C" ) ) {
			sOrderBy = "1,2,4";
			sDescOrdemRel = "Código do cliente";
		}

		try {

			sSql.append( "SELECT C2.CODTIPOCLI,TI.DESCTIPOCLI,C2.RAZCLI,C2.CODCLI," );
			if (detalhado) {
				sSql.append( "V.SERIE, V.DOCVENDA, V.TIPOVENDA, V.CODVENDA, ");
			} 
			sSql.append( "SUM(IV.QTDITVENDA) QTDVENDA, SUM(IV.VLRLIQITVENDA) VLRVENDA " );
			sSql.append( "FROM VDVENDA V, VDITVENDA IV, VDVENDEDOR VD, EQPRODUTO P, EQGRUPO G, " );
			sSql.append( "EQTIPOMOV TM, VDTIPOCLI TI, VDCLIENTE C, VDCLIENTE C2 " );
			sSql.append( sFrom );
			sSql.append( "WHERE V.CODEMP=? AND V.CODFILIAL=? AND " );
			sSql.append( "V.DTEMITVENDA BETWEEN ? AND ? AND " );
			sSql.append( "V.CODEMPCL=C.CODEMP AND V.CODFILIALCL=C.CODFILIAL AND V.CODCLI=C.CODCLI AND " );
			sSql.append( ( cbCliPrinc.getVlrString().equals( "S" ) ? "C2.CODEMP=C.CODEMPPQ AND C2.CODFILIAL=C.CODFILIALPQ AND C2.CODCLI=C.CODPESQ AND " : "C2.CODEMP=C.CODEMP AND C2.CODFILIAL=C.CODFILIAL AND C2.CODCLI=C.CODCLI AND " ) );

			sSql.append( "TI.CODEMP=C2.CODEMPTI AND TI.CODFILIAL=C2.CODFILIALTI AND TI.CODTIPOCLI=C2.CODTIPOCLI AND " );

			// sSql.append( "TI.CODEMP=C2.CODEMPTI AND TI.CODFILIAL=C2.CODFILIALTI AND " );

			sSql.append( "IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND " );
			sSql.append( "IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA=V.TIPOVENDA AND " );
			sSql.append( "VD.CODEMP=V.CODEMPVD AND VD.CODFILIAL=V.CODFILIALVD AND " );
			sSql.append( "VD.CODVEND=V.CODVEND AND ");
			
			if ( bPref ) {
				sSql.append( "C.CODSETOR IS NOT NULL AND " );	
			}
			else {
				sSql.append( "VD.CODSETOR IS NOT NULL AND " );
			}
			
			sSql.append( "P.CODEMP=IV.CODEMPPD AND P.CODFILIAL=IV.CODFILIALPD AND " );
			sSql.append( "P.CODPROD=IV.CODPROD AND G.CODEMP=P.CODEMPGP AND " );
			sSql.append( "G.CODFILIAL=P.CODFILIALGP AND " );
			sSql.append( "TM.CODEMP=V.CODEMPTM AND TM.CODFILIAL=V.CODFILIALTM AND " );
			sSql.append( "TM.CODTIPOMOV=V.CODTIPOMOV " );
			sSql.append( sWhereTM );
			sSql.append( sWhere1 );
			sSql.append( sWhere2 );
			sSql.append( sWhere3 );
			sSql.append( ( sCodGrup1.equals( "" ) ? " AND P.CODGRUP=G.CODGRUP " : " AND SUBSTR(P.CODGRUP,1," + sCodGrup1.length() + ")=G.CODGRUP " ) );
			sSql.append( sWhere );
			sSql.append( "GROUP BY 1,2,3,4 " );
			if (detalhado) {
				sSql.append( ", 5, 6, 7, 8" );
				sSql.append(" ORDER BY " + sOrderBy);
			} else {
				sSql.append( "ORDER BY " + sOrderBy );
			}

			ps = con.prepareStatement( sSql.toString() );
			ps.setInt( iParam++, Aplicativo.iCodEmp );
			ps.setInt( iParam++, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iParam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			if ( !sCodMarca.equals( "" ) ) {
				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQMARCA" ) );
				ps.setString( iParam++, sCodMarca );
			}
			if ( !sCodGrup1.equals( "" ) ) {
				ps.setInt( iParam++, Aplicativo.iCodEmp );
				ps.setInt( iParam++, ListaCampos.getMasterFilial( "EQGRUPO" ) );
				ps.setString( iParam++, sCodGrup1 + ( sCodGrup1.length() < TAM_GRUPO ? "%" : "" ) );
			}
			if ( !sCodGrup2.equals( "" ) ) {
				ps.setString( iParam++, sCodGrup2 );
			}
			if ( iCodSetor != 0 ) {
				ps.setInt( iParam++, iCodSetor );
			}
			if ( iCodVend != 0 ) {
				ps.setInt( iParam++, iCodVend );
			}
			if ( iCodCli != 0 ) {
				ps.setInt( iParam++, iCodCli );
			}
			if ( iCodTipoCli != 0 ) {
				ps.setInt( iParam++, iCodTipoCli );
			}
			rs = ps.executeQuery();
			
			if (postscript) {
				impClienteGrafico( bVisualizar, sFiltros1, sFiltros2, sCab, rs, sDescOrdemRel, fotoemp );
			} else {
				impClienteTexto( bVisualizar, sFiltros1, sFiltros2, sCab, rs, sDescOrdemRel);
				rs.close();
				ps.close();
				con.commit();			
			}
			
		} catch ( Exception err ) {
			err.printStackTrace();
		} finally {
			rs = null;
			sSql = null;
			sWhere = null;
			sWhere1 = null;
			sWhere2 = null;
			sWhere3 = null;
			sWhereTM = null;
			sCab = null;
			sFrom = null;
			sCodMarca = null;
			sCodGrup1 = null;
			sCodGrup2 = null;
			sOrdemRel = null;
			sOrderBy = null;
			sDescOrdemRel = null;
			sFiltros1 = null;
			sFiltros2 = null;
			System.gc();
		}
		
		
	}

	private void impClienteGrafico( TYPE_PRINT bVisualizar, StringBuffer sFiltros1, StringBuffer sFiltros2, 
		String sCab, ResultSet rs, String sDescOrdemRel, Blob fotoemp ) {
		String report = "relatorios/VendasSetor.jasper";
		String label = "Vendas por Setor";
		
	    HashMap<String, Object> hParam = new HashMap<String, Object>();
		//hParam.put( "FILTROS", sFiltros1 + "FILTROS "+ sFiltros2 );
		try {
			hParam.put( "LOGOEMP",  new ImageIcon(fotoemp.getBytes(1, ( int ) fotoemp.length())).getImage() );
		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Erro carregando logotipo !\n" + e.getMessage()  );
			e.printStackTrace();
		}
	
		if ( "D".equals( rgTipoDet.getVlrString() )){
			report = "relatorios/VendasSetorDet.jasper";
			label = "Vendas por Setor";
		}
		FPrinterJob dlGr = new FPrinterJob( report, label, sCab, rs, hParam , this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		} else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de vendas por Setor Detalhado!" + err.getMessage(), true, con, err );
			}
		}
				
	}

	private void impClienteTexto( TYPE_PRINT bVisualizar, StringBuffer sFiltros1, StringBuffer sFiltros2, String sCab, ResultSet rs, String sDescOrdemRel ) {

		if ("D".equals(rgTipoDet.getVlrString() )) {
			Funcoes.mensagemInforma( this, "Relatório Detalhado não disponível para modo texto !" );
			return;
		}
		
		ImprimeOS imp = null;
		int linPag = 0;
		double deVlrTotal = 0;
		double deQtdTotal = 0;
		double deVlrSubTotal = 0;
		double deQtdSubTotal = 0;
		String sCodTipoCli = "";
		String sDescTipoCli = "";
		String sCodTipoCliAnt = "";
		String sDescTipoCliAnt = "";
		
		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.limpaPags();
			imp.setTitulo( "Relatorio de Vendas por Setor x Clientes" );
			if ( sFiltros1.length() > 0 ) {
				imp.addSubTitulo( sFiltros1.toString() );
			}
			if ( sFiltros2.length() > 0 ) {
				imp.addSubTitulo( sFiltros2.toString() );
			}
			if ( sCab.length() > 0 ) {
				imp.addSubTitulo( sCab );
			}
			try {

				while ( rs.next() ) {

					if ( imp.pRow() >= ( linPag - 1 ) ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.incPags();
						imp.eject();
					}

					if ( imp.pRow() == 0 ) {

						imp.impCab( 136, true );
						imp.say( 0, imp.comprimido() );
						imp.say( 1, "|" );
						imp.say( 40, "PERIODO DE: " + txtDataini.getVlrString() + " ATE: " + txtDatafim.getVlrString() + " - Ordem: " + sDescOrdemRel );
						imp.say( 136, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "| RAZAO SOCIAL" );
						imp.say( 55, "| CODIGO" );
						imp.say( 67, "| QUANTIDADE" );
						imp.say( 81, "|     VALOR" );
						imp.say( 99, "|" );
						imp.say( 136, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

					}

					sCodTipoCli = rs.getString( 1 );
					sDescTipoCli = rs.getString( 2 );

					if ( !sCodTipoCli.equals( sCodTipoCliAnt ) ) {

						if ( !sCodTipoCliAnt.equals( "" ) ) {

							sCab = "SUB-TOTAL " + sDescTipoCliAnt.trim() + ":";

							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 1, "|" );
							imp.say( 3, sCab );
							imp.say( 68, "| " + Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( deQtdSubTotal ) ) );
							imp.say( 81, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( deVlrSubTotal ) ) + " |" );
							imp.say( 136, "|" );
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( imp.pRow(), 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );
							deVlrSubTotal = 0;
							deQtdSubTotal = 0;

						}

						sCab = sCodTipoCli + " - " + sDescTipoCli.trim();

						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "|" );
						imp.say( ( ( 136 - sCab.length() ) / 2 ), sCab );
						imp.say( 136, "|" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "+" + StringFunctions.replicate( "-", 133 ) + "+" );

					}

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 1, "|" );
					imp.say( 4, Funcoes.adicionaEspacos( rs.getString( 3 ), 50 ) + " |" );
					imp.say( 56, Funcoes.adicEspacosEsquerda( rs.getString( 4 ), 10 ) + " |" );
					imp.say( 70, Funcoes.adicEspacosEsquerda( String.valueOf( rs.getDouble( 5 ) ), 10 ) + " |" );
					imp.say( 83, Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( 6 ) ) + " |" );
					imp.say( 136, "|" );

					deQtdTotal += rs.getDouble( 5 );
					deVlrTotal += rs.getDouble( 6 );
					deQtdSubTotal += rs.getDouble( 5 );
					deVlrSubTotal += rs.getDouble( 6 );
					sCodTipoCliAnt = sCodTipoCli;
					sDescTipoCliAnt = sDescTipoCli;

				}

				sCab = "SUB-TOTAL " + sDescTipoCliAnt.trim() + ":";

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "|" );
				imp.say( 3, sCab );
				imp.say( 68, "| " + Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( deQtdSubTotal ) ) );
				imp.say( 81, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( deVlrSubTotal ) ) + " |" );
				imp.say( 136, "|" );

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "+" + StringFunctions.replicate( "=", 133 ) + "+" );
				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "| TOTAL" );
				imp.say( 68, "| " + Funcoes.strDecimalToStrCurrency( 10, 2, String.valueOf( deQtdTotal ) ) );
				imp.say( 81, "| " + Funcoes.strDecimalToStrCurrency( 15, 2, String.valueOf( deVlrTotal ) ) + " |" );
				imp.say( 136, "|" );

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 1, "+" + StringFunctions.replicate( "=", 133 ) + "+" );

				imp.eject();
				imp.fechaGravacao();

			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro executando a consulta.\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}

		} catch ( Exception err ) {
			err.printStackTrace();
		} finally {
			rs = null;
			sCab = null;
			sDescOrdemRel = null;
			sFiltros1 = null;
			sFiltros2 = null;
			sCodTipoCli = null;
			sDescTipoCli = null;
			sCodTipoCliAnt = null;
			sDescTipoCliAnt = null;
			imp = null;
			System.gc();
		}

	}

	
	public void valorAlterado( RadioGroupEvent rge ) {

		String sTipoRel = rge.getRadioButton().getText();

		if ( "CLIENTE".equalsIgnoreCase( sTipoRel ) ) {
			rgOrdemRel.setAtivo( true );
		}
		else {
			rgOrdemRel.setAtivo( false );
		}
		
/*		if( rgTipoRel.getVlrString().equals( "P" ) ) {
			txtCodCFOP.setAtivo( true );
			txtCodTipoMov.setAtivo( true );
		} else {
			txtCodCFOP.setAtivo( false );
			txtCodTipoMov.setAtivo( false );		
		}
	*/	
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		
		lcMarca.setConexao( cn );
		lcGrup1.setConexao( cn );
		lcGrup2.setConexao( cn );
		lcSetor.setConexao( cn );
		lcVendedor.setConexao( cn );
		lcCliente.setConexao( cn );
		lcTipoCli.setConexao( cn );
		lcCFOP.setConexao( cn );
		lcMov.setConexao( cn );
		
		bPref = getPrefere();
		
	}

}
