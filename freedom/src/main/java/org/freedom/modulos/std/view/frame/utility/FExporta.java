/**
 * @version 07/2007 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FExporta.java <BR>
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
 *         Formulário de exportação de dados para arquivo, dos dados referentes a contabilidade e livros fiscais.
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.componet.integration.Contabil;
import org.freedom.library.business.componet.integration.FreedomContabil;
import org.freedom.library.business.componet.integration.SafeContabil;
import org.freedom.library.business.componet.integration.SafeContabil.SafeContabilVO;
import org.freedom.library.business.componet.integration.ebs.EbsContabil;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.std.view.dialog.utility.DLChecaExporta;

public class FExporta extends FFilho implements ActionListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelExporta = new JPanelPad();

	private final JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtFile = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private final JButtonPad btChecar = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private final JButtonPad btGerar = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private final JButtonPad btFile = new JButtonPad( Icone.novo( "btAbrirPeq.png" ) );

	private final JProgressBar progress = new JProgressBar();

	private JRadioGroup<?, ?> rgModo;

	private String sistema;

	private Contabil layoutContabil;

	private List<?> erros;

	public FExporta() {

		super( false );
		setTitulo( "Exportar Arquivo" );
		setAtribos( 50, 50, 470, 280 );

		montaRadioGrupos();
		montaTela();

		Calendar cal = Calendar.getInstance();
		txtDtFim.setVlrDate( cal.getTime() );
		cal.set( Calendar.MONTH, cal.get( Calendar.MONTH ) - 1 );
		txtDtIni.setVlrDate( cal.getTime() );

		txtDtIni.setRequerido( true );
		txtDtFim.setRequerido( true );

		btChecar.setToolTipText( "Verificar inconsistências" );
		btGerar.setToolTipText( "Gerar Arquivo" );
		btFile.setToolTipText( "Salvar como" );

		btChecar.addActionListener( this );
		btGerar.addActionListener( this );
		btFile.addActionListener( this );
		txtDtIni.addActionListener( this );

		txtDtFim.addFocusListener( this );

		progress.setStringPainted( true );

		btGerar.setEnabled( false );
	}

	private void montaRadioGrupos() {

		Vector<String> labs = new Vector<String>();
		labs.add( "Contábil" );
		labs.add( "Livros Fiscais" );
		Vector<String> vals = new Vector<String>();
		vals.add( "C" );
		vals.add( "L" );
		rgModo = new JRadioGroup<String, String>( 1, 2, labs, vals );
		rgModo.setVlrString( "C" );
	}

	private void montaTela() {

		getTela().add( panelExporta, BorderLayout.CENTER );

		panelExporta.adic( new JLabel( "Salvar em:" ), 10, 10, 100, 20 );
		panelExporta.adic( txtFile, 10, 30, 400, 20 );
		panelExporta.adic( btFile, 420, 30, 20, 20 );

		panelExporta.adic( new JLabel( "Tipo de exportação :" ), 10, 50, 150, 20 );
		panelExporta.adic( rgModo, 10, 70, 430, 30 );

		JLabel periodo = new JLabel( "Periodo", SwingConstants.CENTER );
		periodo.setOpaque( true );
		panelExporta.adic( periodo, 25, 105, 60, 20 );

		JLabel borda = new JLabel();
		borda.setBorder( BorderFactory.createEtchedBorder() );
		panelExporta.adic( borda, 10, 115, 290, 45 );

		panelExporta.adic( txtDtIni, 25, 130, 110, 20 );
		panelExporta.adic( new JLabel( "até", SwingConstants.CENTER ), 135, 130, 40, 20 );
		panelExporta.adic( txtDtFim, 175, 130, 110, 20 );

		panelExporta.adic( btChecar, 360, 120, 35, 35 );
		panelExporta.adic( btGerar, 405, 120, 35, 35 );

		panelExporta.adic( progress, 10, 180, 430, 20 );

		adicBotaoSair();
	}

	private void getFile() {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

		if ( fileChooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ) {
			txtFile.setVlrString( fileChooser.getSelectedFile().getPath() );
		}

	}

	private String getSistemaContabil() {

		String sistema = "00";

		try {

			String sql = "SELECT SISCONTABIL FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";

			PreparedStatement ps = con.prepareStatement( sql );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				sistema = rs.getString( "SISCONTABIL" );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao verificar preferências!\n" + e.getMessage() );
		}

		return sistema;
	}

	private void checar() {

		if ( txtFile.getVlrString() == null || txtFile.getVlrString().trim().length() < 1 ) {
			Funcoes.mensagemInforma( this, "Selecione o arquivo!" );
			txtFile.requestFocus();
			return;
		}
		if ( txtDtFim.getVlrDate().before( txtDtIni.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final inferior a inicial!" );
			return;
		}

		try {

			executarLayout();

			if ( erros == null ) {
				btChecar.setEnabled( false );
				btGerar.setEnabled( true );
			}
			else {
				btChecar.setEnabled( true );
				btGerar.setEnabled( false );
				showErros();
			}

		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao checar banco de dados!\n" + e.getMessage(), true, con, e );
			e.printStackTrace();
		}
	}

	private void showErros() {

		DLChecaExporta dl = new DLChecaExporta( this, rgModo.getVlrString(), sistema );
		dl.setConexao( con );
		dl.carregaDados( erros );
		dl.setVisible( true );
	}

	private void gerar() {

		try {

			String filename = txtFile.getVlrString().trim();
			File filecontabil = new File( filename );

			layoutContabil.addActionListener( new ActionListener() {

				public void actionPerformed( ActionEvent e ) {

					if ( e.getSource().equals( layoutContabil ) ) {
						if ( Contabil.SET_SIZE_ROWS.equals( e.getActionCommand() ) ) {
							progress.setMaximum( layoutContabil.getSizeMax() );
						}
						else if ( Contabil.PROGRESS_IN_ROWS.equals( e.getActionCommand() ) ) {
							progress.setValue( layoutContabil.getProgress() );
							progress.setVisible( true );
						}
					}
				}
			} );

			layoutContabil.createFile( filecontabil );

			Funcoes.mensagemInforma( this, "Arquivo exportado com sucesso!" );
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao montar arquivo!\n" + e.getMessage(), true, con, e );
			e.printStackTrace();
		}

		progress.setValue( 0 );
	}

	private void executarLayout() throws Exception {

		sistema = getSistemaContabil();

		if ( "C".equals( rgModo.getVlrString() ) ) {

			if ( Contabil.FREEDOM_CONTABIL.equals( sistema ) ) {

				getLayoutFreedom();
			}
			else if ( Contabil.SAFE_CONTABIL.equals( sistema ) ) {

				erros = getLayoutSafe();

				if ( erros == null || erros.size() == 0 ) {
					erros = null;
				}
			}
			else if ( Contabil.EBS_CONTABIL.equals( sistema ) ) {

				getLayoutEbs();
			}
			else {
				Funcoes.mensagemErro( this, "Sistema de exportação não definido!\n" + "Verifique o cadastro de preferências do sistema." );
				dispose();
			}
		}
	}

	private void getLayoutFreedom() {

		try {

			layoutContabil = new FreedomContabil();
			( (FreedomContabil) layoutContabil ).execute( con, txtDtIni.getVlrDate(), txtDtFim.getVlrDate() );

		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao buscar dados para sistema Freedom Contábil!" );
			e.printStackTrace();
		}
	}

	private List<SafeContabilVO> getLayoutSafe() {

		List<SafeContabilVO> erros = null;

		try {

			layoutContabil = new SafeContabil();
			erros = ( (SafeContabil) layoutContabil ).execute( con, txtDtIni.getVlrDate(), txtDtFim.getVlrDate() );

		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao buscar dados para sistema Safe Contábil!" );
			e.printStackTrace();
		}

		return erros;
	}

	private List<Contabil> getLayoutEbs() {

		List<Contabil> erros = null;

		try {

			layoutContabil = new EbsContabil();
			( (EbsContabil) layoutContabil ).execute( con, txtDtIni.getVlrDate(), txtDtFim.getVlrDate() );

		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao buscar dados para sistema Cordilheira Contábil!" );
			e.printStackTrace();
		}

		return erros;
	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btGerar ) {
			Thread th = new Thread( new Runnable() {

				public void run() {

					gerar();
				}
			} );
			th.start();
		}
		else if ( e.getSource() == btChecar ) {
			checar();
		}
		else if ( e.getSource() == btFile ) {
			getFile();
		}
		else if ( e.getSource() == txtDtIni ) {
			btChecar.setEnabled( true );
			btGerar.setEnabled( false );
		}
	}

	public void focusGained( FocusEvent e ) {

	}

	public void focusLost( FocusEvent e ) {

		if ( e.getSource() == txtDtFim ) {
			btChecar.requestFocus();
		}
	}

	@ Override
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
	}

}
