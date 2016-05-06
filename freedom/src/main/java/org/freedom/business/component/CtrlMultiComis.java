package org.freedom.business.component;

import java.awt.Component;
import java.math.BigDecimal;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.EditEvent;
import org.freedom.acao.EditListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;

public class CtrlMultiComis implements CarregaListener {

	private int numComissionados = 1;

	private DbConnection con = null;

	private ItemComis[] listComis = null;

	private String cpTipovenda = null;

	private String cpCodvenda = null;

	private int codvenda = -1;

	private String tipovenda = "";

	private JTextFieldPad txtTipovendaPrinc = null;

	private JTextFieldPad txtCodvendaPrinc = null;

	private JTextFieldPad txtCodvendPrinc = null;

	private ListaCampos lcMaster = null;

	private Component owner = null;

	private String vendacomis = null;

	public CtrlMultiComis( final java.awt.Component owner, final DbConnection con, final ListaCampos lcMaster, final int numComissionados, final String cpTipovenda, final String cpCodvenda, final JTextFieldPad txtTipovenda, final JTextFieldPad txtCodvenda, final JTextFieldPad txtCodvendPrinc,
			final String vendacomis ) {

		this.con = con;
		this.cpTipovenda = cpTipovenda;
		this.cpCodvenda = cpCodvenda;
		this.txtTipovendaPrinc = txtTipovenda;
		this.txtCodvendaPrinc = txtCodvenda;
		this.numComissionados = numComissionados;
		this.txtCodvendPrinc = txtCodvendPrinc;
		this.owner = owner;
		this.vendacomis = vendacomis;
		this.lcMaster = lcMaster;
		// if (lcMaster!=null) {
		// lcMaster.addCarregaListener( this );
		// }

	}

	public boolean isEnabled() {

		boolean result = false;
		if ( listComis != null ) {
			for ( ItemComis itemcomis : listComis ) {
				if ( ( itemcomis != null ) && ( itemcomis.getTxtCodvend().isEnabled() ) ) {
					result = true;
					break;
				}
			}
		}
		return false;
	}

	public void loadVendaComis( final String tipovenda, final int codvenda, final int codregrcomis ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int pos = 0;
		if ( listComis == null ) {
			listComis = new ItemComis[ numComissionados ];
			for ( int i = 0; i < numComissionados; i++ ) {
				listComis[ i ] = new ItemComis();
			}
		}
		try {
			for ( ItemComis itemcomis : listComis ) {
				itemcomis.limpa();
			}
			ps = con.prepareStatement( "SELECT VC.SEQVC, VC.CODREGRCOMIS, " 
					+ "VC.SEQITRC, VC.CODVEND, VC.PERCVC " 
					+ "FROM VDVENDACOMIS VC " 
					+ "WHERE VC.CODEMP=? AND VC.CODFILIAL=? AND " 
					+ "VC." + cpTipovenda + "=? AND VC." 
					+ cpCodvenda 
					+ "=?" );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VD" + vendacomis ) );
			ps.setString( 3, tipovenda );
			ps.setInt( 4, codvenda );
			rs = ps.executeQuery();
			while ( rs.next() ) {
				listComis[ pos ].getTxtSeqvc().setVlrInteger( rs.getInt( "SEQVC" ) );
				listComis[ pos ].setSeqitrc( rs.getInt( "SEQITRC" ) );
				listComis[ pos ].getTxtCodvend().setVlrInteger( rs.getInt( "CODVEND" ) );
				listComis[ pos ].getTxtTipovenda().setVlrString( tipovenda );
				listComis[ pos ].getTxtCodvenda().setVlrInteger( codvenda );
				listComis[ pos ].getTxtPerccomis().setVlrBigDecimal( rs.getBigDecimal( "PERCVC" ) );
				// listComis[ pos ].getLcVendaComis().carregaDados();
				pos++;
			}
			for ( ItemComis itemcomis : listComis ) {
				if ( itemcomis != null ) {
					itemcomis.getLcVendaComis().carregaDados();
				}
			}
			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	public class ItemComis implements InsertListener, CarregaListener, EditListener {

		private int seqitrc = 0;

		private float perccomis = 0;

		private String obrigitrc = null;

		private JTextFieldPad txtTipovenda = null;

		private JTextFieldPad txtCodvenda = null;

		private JTextFieldPad txtSeqvc = null;

		private JTextFieldPad txtCodvend = null;

		private JTextFieldFK txtNomevend = null;

		private JTextFieldPad txtCodregrcomis = null;

		private JTextFieldPad txtPerccomis = null;

		private JLabelPad lbCodvend = new JLabelPad( "Cód.comis." );

		private JLabelPad lbNomevend = new JLabelPad( "Nome do comissionado" );

		private JLabelPad lbPercvend = new JLabelPad( "% comis." );

		private ListaCampos lcVend = new ListaCampos( owner, "VD" );

		private ListaCampos lcVendaComis = new ListaCampos( owner, "VC" );

		public ItemComis() {

			lcVend.setConexao( con );
			lcVendaComis.setConexao( con );
			// lcVendaComis.setMaster( lcMaster );
			// lcMaster.adicDetalhe( lcVendaComis );
			// lcVendaComis.setQueryCommit( false );
			txtTipovenda = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
			txtCodvenda = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
			txtSeqvc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
			txtCodvend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
			txtCodregrcomis = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
			txtNomevend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
			txtPerccomis = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 7, Aplicativo.casasDecFin );
			txtCodvend.setNomeCampo( "codvend" );
			txtTipovenda.setNomeCampo( cpTipovenda );
			txtCodvenda.setNomeCampo( cpCodvenda );
			txtSeqvc.setNomeCampo( "seqvc" );
			txtCodregrcomis.setNomeCampo( "codregrcomis" );
			txtPerccomis.setNomeCampo( "percvc" );
			lcVend.add( new GuardaCampo( txtCodvend, "Codvend", "Cód.comis.", ListaCampos.DB_PK, false ) );
			lcVend.add( new GuardaCampo( txtNomevend, "Nomevend", "Nome do comissionado", ListaCampos.DB_SI, false ) );
			lcVend.setWhereAdic( "ATIVOCOMIS='S'" );
			lcVend.montaSql( false, "VENDEDOR", "VD" );
			// lcVend.setQueryCommit( false );
			lcVend.setReadOnly( true );
			txtCodvend.setTabelaExterna( lcVend, null );
			lcVendaComis.addInsertListener( this );
			lcVend.addEditListener( this );
			// lcMaster.addPostListener( this );
		}

		public int getSeqitrc() {

			return seqitrc;
		}

		public void setSeqitrc( int seqitrc ) {

			this.seqitrc = seqitrc;
		}

		public String getObrigitrc() {

			return obrigitrc;
		}

		public void setObrigitrc( String obrigitrc ) {

			this.obrigitrc = obrigitrc;
			if ( "S".equals( obrigitrc ) ) {
				txtCodvend.setRequerido( true );
				txtPerccomis.setRequerido( true );
			}
		}

		public void setEnabled( final boolean enabled ) {

			txtCodvend.setEnabled( enabled );
			txtPerccomis.setEnabled( enabled );
		}

		public void limpa() {

			txtCodvend.setVlrString( "" );
			txtPerccomis.setVlrString( "" );
			txtNomevend.setVlrString( "" );
		}

		public JTextFieldPad getTxtCodvend() {

			return txtCodvend;
		}

		public void setTxtCodvend( JTextFieldPad txtCodvend ) {

			this.txtCodvend = txtCodvend;
		}

		public JTextFieldFK getTxtNomevend() {

			return txtNomevend;
		}

		public void setTxtNomevend( JTextFieldFK txtNomevend ) {

			this.txtNomevend = txtNomevend;
		}

		public JLabelPad getLbCodvend() {

			return lbCodvend;
		}

		public void setLbCodvend( JLabelPad lbCodvend ) {

			this.lbCodvend = lbCodvend;
		}

		public JLabelPad getLbNomevend() {

			return lbNomevend;
		}

		public void setLbNomevend( JLabelPad lbNomevend ) {

			this.lbNomevend = lbNomevend;
		}

		public JTextFieldPad getTxtPerccomis() {

			return txtPerccomis;
		}

		public void setTxtPerccomis( JTextFieldPad txtPerccomis ) {

			this.txtPerccomis = txtPerccomis;
		}

		public JLabelPad getLbPercvend() {

			return lbPercvend;
		}

		public void setLbPercvend( JLabelPad lbPercvend ) {

			this.lbPercvend = lbPercvend;
		}

		public JTextFieldPad getTxtSeqvc() {

			return txtSeqvc;
		}

		public void setTxtSeqvc( JTextFieldPad txtSeqvc ) {

			this.txtSeqvc = txtSeqvc;
		}

		public ListaCampos getLcVendaComis() {

			return lcVendaComis;
		}

		public void setLcVendaComis( ListaCampos lcVendaComis ) {

			this.lcVendaComis = lcVendaComis;
		}

		public JTextFieldPad getTxtTipovenda() {

			return txtTipovenda;
		}

		public void setTxtTipovenda( JTextFieldPad txtTipovenda ) {

			this.txtTipovenda = txtTipovenda;
		}

		public JTextFieldPad getTxtCodvenda() {

			return txtCodvenda;
		}

		public void setTxtCodvenda( JTextFieldPad txtCodvenda ) {

			this.txtCodvenda = txtCodvenda;
		}

		public float getPerccomis() {

			return perccomis;
		}

		public void setPerccomis( float perccomis ) {

			this.perccomis = perccomis;
		}

		public void afterInsert( InsertEvent ievt ) {

			if ( ievt.getListaCampos() == lcVendaComis ) {
				txtPerccomis.setVlrBigDecimal( new BigDecimal( getPerccomis() ) );
			}
		}

		public void beforeInsert( InsertEvent ievt ) {

		}

		public void afterPost( PostEvent pevt ) {

			if ( pevt.getListaCampos() == lcMaster ) {
			}
		}

		public void beforePost( PostEvent pevt ) {

			if ( pevt.getListaCampos() == lcMaster ) {
				/*
				 * if (!validaItens()) { pevt.cancela(); return; }
				 */
			}
		}

		public void afterCarrega( CarregaEvent cevt ) {

		}

		public void beforeCarrega( CarregaEvent cevt ) {

		}

		public ListaCampos getLcVend() {

			return lcVend;
		}

		public void setLcVend( ListaCampos lcVend ) {

			this.lcVend = lcVend;
		}

		public void afterEdit( EditEvent eevt ) {

			if ( eevt.getListaCampos() == lcVend ) {
				if ( ( lcMaster != null ) && ( lcMaster.getStatus() == ListaCampos.LCS_SELECT ) ) {
					lcMaster.edit();
				}
			}

		}

		public void beforeEdit( EditEvent eevt ) {

		}

		public void edit( EditEvent eevt ) {

		}

		public JTextFieldPad getTxtCodregrcomis() {

			return txtCodregrcomis;
		}

		public void setTxtCodregrcomis( JTextFieldPad txtCodregrcomis ) {

			this.txtCodregrcomis = txtCodregrcomis;
		}
	}

	public ItemComis[] getListComis() {

		return this.listComis;
	}

	public void loadRegraComis( final int codregrcomis ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		int pos = 0;
		if ( listComis == null ) {
			listComis = new ItemComis[ numComissionados ];
			for ( int i = 0; i < numComissionados; i++ ) {
				listComis[ i ] = new ItemComis();
			}
		}
		try {
			ps = con.prepareStatement( "SELECT IR.SEQITRC, IR.OBRIGITRC, IR.PERCCOMISITRC " + "FROM VDITREGRACOMIS IR " + "WHERE IR.CODEMP=? AND IR.CODFILIAL=? AND IR.CODREGRCOMIS=? " );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDITREGRACOMIS" ) );
			ps.setInt( 3, codregrcomis );
			rs = ps.executeQuery();
			while ( rs.next() ) {
				listComis[ pos ].setEnabled( true );
				listComis[ pos ].setSeqitrc( rs.getInt( "SEQITRC" ) );
				listComis[ pos ].setObrigitrc( rs.getString( "OBRIGITRC" ) );
				listComis[ pos ].setPerccomis( rs.getFloat( "PERCCOMISITRC" ) );
				pos++;
			}
			for ( int i = pos; i < numComissionados; i++ ) {
				listComis[ i ].limpa();
				listComis[ i ].setEnabled( false );
			}

		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	public boolean validaItens() {

		boolean result = true;
		if ( listComis != null ) {
			for ( ItemComis itemcomis : listComis ) {
				if ( ( itemcomis != null ) ) {
					if ( ( "S".equals( itemcomis.getObrigitrc() ) ) && ( itemcomis.getTxtCodvend().isEnabled() ) ) {
						if ( "".equals( itemcomis.getTxtCodvend().getVlrString() ) ) {
							Funcoes.mensagemInforma( owner, "Preencha o comissionado!" );
							itemcomis.getTxtCodvend().requestFocus();
							result = false;
							break;
						}
						if ( "".equals( itemcomis.getTxtPerccomis().getVlrString() ) ) {
							Funcoes.mensagemInforma( owner, "Preencha o % de comissao!" );
							itemcomis.getTxtCodvend().requestFocus();
							result = false;
							break;
						}
					}
				}
			}
		}
		return result;
	}

	public void salvaItens( final String tipovenda, final int codvenda, final int codregrcomis ) {

		if ( listComis != null ) {
			for ( ItemComis itemcomis : listComis ) {
				if ( ( itemcomis != null ) ) {
					if ( ( "S".equals( itemcomis.getObrigitrc() ) ) && ( itemcomis.getTxtCodvend().isEnabled() ) ) {
						if ( ( itemcomis.getLcVendaComis().getStatus() == ListaCampos.LCS_EDIT ) || ( itemcomis.getLcVendaComis().getStatus() == ListaCampos.LCS_INSERT ) ) {
							itemcomis.getTxtTipovenda().setVlrString( tipovenda );
							itemcomis.getTxtCodvenda().setVlrInteger( codvenda );
							itemcomis.getTxtCodregrcomis().setVlrInteger( codregrcomis );
							itemcomis.getTxtSeqvc().setVlrInteger( itemcomis.getSeqitrc() );
							itemcomis.getLcVendaComis().post();
						}
					}
				}
			}
		}
	}

	synchronized public void salvaVendaComis( final String tipovenda, final int codvenda, final int codregrcomis ) {

		PreparedStatement ps = null;
		PreparedStatement psu = null;
		PreparedStatement psi = null;
		PreparedStatement psd = null;
		ResultSet rs = null;
		try {
			for ( ItemComis itemcomis : listComis ) {
				ps = con.prepareStatement( "SELECT VC.SEQVC, VC.CODREGRCOMIS, " + "VC.SEQITRC, VC.CODVEND, VC.PERCVC " + "FROM VD" + vendacomis + " VC " + "WHERE VC.CODEMP=? AND VC.CODFILIAL=? AND " + "VC." + cpTipovenda + "=? AND VC." + cpCodvenda + "=? AND " + "VC.SQVC=?" );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "VD" + vendacomis ) );
				ps.setString( 3, tipovenda );
				ps.setInt( 4, codvenda );
				ps.setInt( 5, itemcomis.getTxtSeqvc().getVlrInteger().intValue() );
				rs = ps.executeQuery();
				if ( rs.next() ) {
					if ( itemcomis.getTxtCodvend().getVlrInteger().intValue() == 0 ) {
						psd = con.prepareStatement( "DELETE FROM VD" + vendacomis + " WHERE VC.CODEMP=? AND VC.CODFILIAL=? AND " + "VC." + cpTipovenda + "=? AND VC." + cpCodvenda + "=? AND " + "VC.SQVC=?" );
						psd.setInt( 1, Aplicativo.iCodEmp );
						psd.setInt( 2, ListaCampos.getMasterFilial( "VD" + vendacomis ) );
						psd.setString( 3, tipovenda );
						psd.setInt( 4, codvenda );
						psd.setInt( 5, itemcomis.getTxtSeqvc().getVlrInteger().intValue() );
						psd.executeUpdate();
					}
					else {
						psu = con.prepareStatement( "UPDATE VD" + vendacomis + " SET CODEMPRC=?, CODFILIALRC=?, CODREGRCOMIS=?, SEQITRC=?, " + "CODEMPVD=?, CODFILIALVD=?, CODVEND=?, PERCVC=? " + "WHERE VC.CODEMP=? AND VC.CODFILIAL=? AND " + "VC." + cpTipovenda + "=? AND VC." + cpCodvenda
								+ "=? AND " + "VC.SQVC=?" );
						psu.setInt( 1, Aplicativo.iCodEmp );
						psu.setInt( 2, ListaCampos.getMasterFilial( "VDITREGRACOMIS" ) );
						psu.setInt( 3, codregrcomis );
						psu.setInt( 4, itemcomis.getSeqitrc() );
						psu.setInt( 5, Aplicativo.iCodEmp );
						psu.setInt( 6, ListaCampos.getMasterFilial( "VDVENDEDOR" ) );
						psu.setInt( 7, itemcomis.getTxtCodvend().getVlrInteger().intValue() );
						psu.setBigDecimal( 8, itemcomis.getTxtPerccomis().getVlrBigDecimal() );
						psu.setInt( 9, Aplicativo.iCodEmp );
						psu.setInt( 10, ListaCampos.getMasterFilial( "VD" + vendacomis ) );
						psu.setString( 11, tipovenda );
						psu.setInt( 12, codvenda );
						psu.setInt( 13, itemcomis.getTxtSeqvc().getVlrInteger().intValue() );
						psu.executeUpdate();
					}
				}
				else {
					if ( itemcomis.getTxtCodvend().getVlrInteger().intValue() != 0 ) {
						psi = con.prepareStatement( "INSERT INTO VD" + vendacomis + " (CODEMP, CODFILIAL, TIPOVENDA, CODVENDA, SEQVC, " + "CODEMPRC, CODFILIALRC, CODREGRCOMIS, SEQITRC, CODEMPVD, " + "CODFILIALVD, CODVEND, PERCVC) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" );
						psi.setInt( 1, Aplicativo.iCodEmp );
						psi.setInt( 2, ListaCampos.getMasterFilial( "VD" + vendacomis ) );
						psi.setString( 3, tipovenda );
						psi.setInt( 4, codvenda );
						psi.setInt( 5, itemcomis.getTxtSeqvc().getVlrInteger().intValue() );
						psi.setInt( 6, Aplicativo.iCodEmp );
						psi.setInt( 7, ListaCampos.getMasterFilial( "VDITREGRACOMIS" ) );
						psi.setInt( 8, codregrcomis );
						psi.setInt( 9, itemcomis.getSeqitrc() );
						psi.setInt( 10, Aplicativo.iCodEmp );
						psi.setInt( 11, ListaCampos.getMasterFilial( "VDVENDEDOR" ) );
						psi.setInt( 12, itemcomis.getTxtCodvend().getVlrInteger().intValue() );
						psi.setBigDecimal( 13, itemcomis.getTxtPerccomis().getVlrBigDecimal() );
						psi.executeUpdate();
					}
				}
			}
			if ( rs != null ) {
				rs.close();
			}
			if ( ps != null ) {
				ps.close();
			}
			if ( psu != null ) {
				psu.close();
			}
			if ( psi != null ) {
				psi.close();
			}
			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	public void afterCarrega( CarregaEvent cevt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

}
