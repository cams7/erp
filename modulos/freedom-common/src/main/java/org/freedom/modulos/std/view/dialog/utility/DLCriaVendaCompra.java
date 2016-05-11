package org.freedom.modulos.std.view.dialog.utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.gms.business.object.TipoMov;
import org.freedom.modulos.lvf.business.object.SeqSerie;

public class DLCriaVendaCompra extends FDialogo implements CarregaListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtNewCod = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldFK.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldFK.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodModNota = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtDtSaidaEntrega = new JTextFieldPad( JTextFieldPad.TP_DATE, 8, 0 );
	
	private JTextFieldPad txtDataAtual = new JTextFieldPad( JTextFieldPad.TP_DATE, 8, 0 );

	private JTextFieldPad txtDoc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtEmitNota = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtTipoMov = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private ListaCampos lcPlanoPag = new ListaCampos( this );

	private ListaCampos lcTipoMov = new ListaCampos( this );

	private ListaCampos lcSerie = new ListaCampos( this, "SE" );
	
	private boolean cancelaEvento = false;
	
	private boolean soldtsaida = false;

	public DLCriaVendaCompra( boolean confirmacodigo, String tipo, boolean soldtsaida ) {
		
		this.soldtsaida = soldtsaida;

		setTitulo( "Confirmação" );

		String labeltipo = "";

		if ( "E".equals( tipo ) ) { // Venda PDV/ECF
			setAtribos( 235, 120 );
			labeltipo = "um cupom";
			adic( new JLabelPad( "Deseja criar " + labeltipo + " agora?" ), 7, 15, 220, 20 );
		}
		else if ( "V".equals( tipo ) ) { // Venda STD
			setAtribos( 235, 140 );
			labeltipo = "uma venda";
		}
		else if ( "C".equals( tipo ) ) { // Compra GMS
			setAtribos( 550, 160 );
			labeltipo = "uma compra";
		}

		// Se for uma compra ou venda e deve ser confirmado o codigo
//		if ( ( "V".equals( tipo ) ) && confirmacodigo ) {
		if ( "V".equals( tipo ) ) {

			adic( new JLabelPad( "Deseja criar " + labeltipo + " agora?" ), 7, 15, 220, 20 );

			adic( new JLabelPad( "Nº Pedido" ), 7, 40, 80, 20 );
			adic( txtNewCod, 87, 40, 120, 20 ); 
			
			if (soldtsaida) {
				setAtribos( 235, 180 );
				labeltipo = "uma venda";
				adic( new JLabelPad( "Dt.Saída" ), 7, 80, 80, 20 );
				adic( txtDtSaidaEntrega, 87, 80, 120, 20);
				txtDtSaidaEntrega.setRequerido( true );
			}
		}

		// Se for uma compra
		if ( "C".equals( tipo ) ) {

			adic( new JLabelPad( "Cod.Compra" ), 7, 0, 80, 20 );
			adic( txtNewCod, 7, 20, 80, 20 );

			adic( new JLabelPad( "Cód.tp.mov." ), 90, 0, 80, 20 );
			adic( txtCodTipoMov, 90, 20, 80, 20 );

			adic( new JLabelPad( "Descrição do tipo de movimento" ), 173, 0, 200, 20 );
			adic( txtDescTipoMov, 173, 20, 200, 20 );

			adic( new JLabelPad( "Serie" ), 376, 0, 60, 20 );
			adic( txtSerie, 376, 20, 60, 20 );

			adic( new JLabelPad( "Nro.Doc." ), 439, 0, 80, 20 );
			adic( txtDoc, 439, 20, 80, 20 );

			adic( new JLabelPad( "Cód.p.pag." ), 7, 40, 80, 20 );
			adic( txtCodPlanoPag, 7, 60, 80, 20 );

			adic( new JLabelPad( "Descrição do plano de pagamento" ), 90, 40, 200, 20 );
			adic( txtDescPlanoPag, 90, 60, 284, 20 );

		}

		lcSerie.addCarregaListener( this );

	}

	private void montaListaCampos() {

		// Lista campos para plano de pagamento
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag.setWhereAdic( "ATIVOPLANOPAG='S' AND CVPLANOPAG IN ('C','A')" );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setQueryCommit( false );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );

		// Lista campos para o tipo de movimento
		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.add( new GuardaCampo( txtCodModNota, "CodModNota", "Código do modelo de nota", ListaCampos.DB_FK, false ) );
		lcTipoMov.add( new GuardaCampo( txtTipoMov, "TipoMov", "Tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.add( new GuardaCampo( txtSerie, "serie", "serie", ListaCampos.DB_FK, false ) );
		lcTipoMov.add( new GuardaCampo( txtEmitNota, "EmitNfCpMov", "emit.nota", ListaCampos.DB_SI, false ) );

		lcTipoMov.setWhereAdic( "((ESTIPOMOV = 'E') AND" + " ( TUSUTIPOMOV='S' OR	EXISTS (SELECT * FROM EQTIPOMOVUSU TU " + "WHERE TU.CODEMP=EQTIPOMOV.CODEMP AND TU.CODFILIAL=EQTIPOMOV.CODFILIAL AND " + "TU.CODTIPOMOV=EQTIPOMOV.CODTIPOMOV AND TU.CODEMPUS=" + Aplicativo.iCodEmp
				+ " AND TU.CODFILIALUS=" + ListaCampos.getMasterFilial( "SGUSUARIO" ) + " AND TU.IDUSU='" + Aplicativo.getUsuario().getIdusu() + "') ) )" );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, null );
		txtCodTipoMov.setFK( true );
		txtCodTipoMov.setNomeCampo( "codtipomov" );

		// Lista campos para a serie da compra
		lcSerie.add( new GuardaCampo( txtSerie, "Serie", "Série", ListaCampos.DB_PK, false ) );
		lcSerie.add( new GuardaCampo( txtDoc, "DocSerie", "Doc", ListaCampos.DB_SI, false ) );
		lcSerie.montaSql( false, "SERIE", "LF" );
		lcSerie.setQueryCommit( false );
		lcSerie.setReadOnly( true );
		txtSerie.setTabelaExterna( lcSerie, null );

	}

	public void setNewCodigo( int arg ) {

		txtNewCod.setVlrInteger( arg );
	}

	public int getNewCodigo() {

		return txtNewCod.getVlrInteger().intValue();
	}
	
	public void setDataSaida( Date arg ) {
		txtDtSaidaEntrega.setVlrDate( arg );
	}

	public Date getDataSaida() {
		return txtDtSaidaEntrega.getVlrDate();
	}
	
/*
	public void keyPressed( KeyEvent kevt ) {
		

		if ( kevt.getSource() == btOK ) {
				btOK.doClick();
		}

		super.keyPressed( kevt );

	}
*/
		
	public Integer getCodplanopag() {

		return txtCodPlanoPag.getVlrInteger();
	}

	public void setCodplanopag( Integer codplanopag ) {

		txtCodPlanoPag.setVlrInteger( codplanopag );
	}

	public String getSerie() {

		return txtSerie.getVlrString();
	}

	public void setSerie( String serie ) {

		txtSerie.setVlrString( serie );
	}

	public void setConexao( DbConnection cn ) {

		lcPlanoPag.setConexao( cn );
		lcTipoMov.setConexao( cn );
		lcSerie.setConexao( cn );

		montaListaCampos();

		lcPlanoPag.carregaDados();
		lcTipoMov.carregaDados();

	}

	public Integer getDoc() {

		return txtDoc.getVlrInteger();
	}

	public Integer getCodTipoMov() {

		return txtCodTipoMov.getVlrInteger();
	}

	public void setCodTipoMov( Integer codtipomov ) {

		txtCodTipoMov.setVlrInteger( codtipomov );
	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcSerie ) {
			try {
				// Set for um pedido de compra deve gerar o nro do documento automaticamente e bloquear os campos ;
				if ( TipoMov.TM_PEDIDO_COMPRA.getValue().equals( txtTipoMov.getVlrString() ) && "S".equals( txtEmitNota.getVlrString() ) ) {

					SeqSerie ss = new SeqSerie( txtSerie.getVlrString() );
					txtDoc.setVlrInteger( ss.getDocserie() + 1 );
					txtSerie.setAtivo( false );
					txtDoc.setAtivo( false );
				}

			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}
	
	@ Override
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if (soldtsaida) {
				if(txtDtSaidaEntrega.getVlrDate() != null) {
					txtDataAtual.setVlrDate( new Date() );
					
					if ( txtDtSaidaEntrega.getVlrDate().compareTo(txtDataAtual.getVlrDate()) >= 0 ){
						OK = true;
						setVisible(false);
					}
					else {
						Funcoes.mensagemInforma( this, "Campo data de saída/entrega não pode ser inferior a data atual, Verifique!!!" );
						txtDtSaidaEntrega.requestFocus();
						OK = false;
					} 
				} else {
					Funcoes.mensagemInforma( this, "Campo data de saída/entrega é requerido!!!" );
					txtDtSaidaEntrega.requestFocus();
					OK = false;
				}
			} else {
				super.actionPerformed( evt );
			}
		}
		
		if (evt.getSource() == btCancel) {
			OK = false;
			setVisible(false);
		}	 
	}
}
