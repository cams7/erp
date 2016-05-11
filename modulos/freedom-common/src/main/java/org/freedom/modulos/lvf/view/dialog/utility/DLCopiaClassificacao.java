package org.freedom.modulos.lvf.view.dialog.utility;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.lvf.dao.DAOClFiscal;


public class DLCopiaClassificacao  extends FFDialogo implements CarregaListener {
	
	private static final long serialVersionUID = 1L;
	
	private String codfisc;
	
	private final JTextFieldPad txtCodFisc = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private DAOClFiscal daofisc = null;
	
	public DLCopiaClassificacao(){
		super();
	}
	
	public DLCopiaClassificacao(String codfisc){
		this.codfisc = codfisc;
		txtCodFisc.setVlrString( codfisc );
		montaTela();
	}
	
	public void montaTela(){
		setTitulo( "Copia Classificação Fiscal" );
		setAtribos( 50, 50, 450, 200 );
		
		adic( txtCodFisc , 7, 25, 100, 20, "Cód.class.fiscal" );
	}
	
		
	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {

	}
	
	public void actionPerformed( ActionEvent evt ) {
		boolean result = false;
		if (evt.getSource()==btOK) {
			if(codfisc.equals( txtCodFisc.getVlrString()) || "".equals( txtCodFisc.getVlrString())) {
				Funcoes.mensagemInforma( this, "Altere o código da classificação fiscal." );
				return;
			} else {
			    result = copiaClassificacao(Aplicativo.iCodEmp, ListaCampos.getMasterFilial( "LFCLFISCAL" ), txtCodFisc.getVlrString());			
			}
			
     	} else if (evt.getSource()==btCancel) {
			result = true;
		}
		if ( result ) {
			super.actionPerformed( evt );
		}
	}
	
	public boolean copiaClassificacao( Integer codemp, Integer codfilial, String novocodfisc ){
		boolean result = false;
		
		try {
			result = daofisc.cloneClFiscal(codemp, codfilial, novocodfisc, codfisc);
					
			daofisc.cloneItClFiscal( codemp, codfilial, novocodfisc, codfisc  );
			
			codfisc = novocodfisc;
			
			con.commit();
			
		} catch ( SQLException e ) {
			try {
				con.rollback();
			} catch ( SQLException e1 ) {
				e1.printStackTrace();
			}
			
			Funcoes.mensagemErro( this, "Não foi possivel copiar classificação fiscal" );
			e.printStackTrace();
		}
	
		return true;
	}
	
	public void setConexao( DbConnection cn ) {
		super.setConexao( cn );
		daofisc = new DAOClFiscal(cn);
	}
	
	public String getCodfisc() {
		
		return codfisc;
	}
	

	public void setCodfisc( String codfisc ) {
	
		this.codfisc = codfisc;
	}

}
