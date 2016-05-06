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

public class DLCriaContrato extends FDialogo implements CarregaListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtNewCod = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescContrato = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private Integer codCli;

	private String nomeCli;

	private Integer codContr;


	public DLCriaContrato( Integer codContr, Integer codCli, String nomeCli ) {
		
		this.codCli = codCli;
		this.nomeCli = nomeCli;
		this.codContr = codContr;

		setTitulo( "Confirmação" );

		String labeltipo = "";
		setAtribos( 300, 200 );
		labeltipo = "uma venda";

		// Se for uma compra ou venda e deve ser confirmado o codigo
		//		if ( ( "V".equals( tipo ) ) && confirmacodigo ) {
		adic( new JLabelPad( "Deseja criar " + labeltipo + " agora?" ), 7, 15, 220, 20 );

		adic( new JLabelPad( "Nº Pedido" ), 7, 40, 80, 20 );
		adic( txtNewCod, 87, 40, 120, 20 ); 
		
		adic( new JLabelPad( "Descrição do Projeto" ), 7, 63, 250, 20 );
		adic( txtDescContrato, 7, 83, 250, 20 ); 
		
		montaDescProd();
	}

	private void montaDescProd() {
		
		String desccontr = " Contrato número " + codContr + " - "  + nomeCli ;
	
		txtDescContrato.setVlrString( desccontr );

	}

	public void setNewCodigo( int arg ) {

		txtNewCod.setVlrInteger( arg );
	}

	public int getNewCodigo() {

		return txtNewCod.getVlrInteger().intValue();
	}
	
	public String getDescContr() {
		return txtDescContrato.getVlrString();
	}
	

	@ Override
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if (!"".equals( txtDescContrato.getVlrString())){
				OK = true;
				setVisible(false);
			}
			else {
				Funcoes.mensagemInforma( this, "Descrição do contato é um campo requerido!!!" );
				txtDescContrato.requestFocus();
				OK = false;
			} 
		}

		if (evt.getSource() == btCancel) {
			OK = false;
			setVisible(false);
		}	 
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub
		
	}

	public void afterCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub
		
	}
}
