/**
 * 
 */
package fdi.ucm.server.importparser.chasqui.coleccion.categoria;

import java.sql.ResultSet;
import java.sql.SQLException;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteFile;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteResourceElementFile;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteResourceElementType;

/**
 * Clase que implementa la creacion de los files y de sus atributos.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class Grammar_File implements InterfaceChasquiparser{

	private CompleteGrammar Attributo;
//	private MetaText PATH;
	private CompleteResourceElementType FireRef;
	private LoadCollectionChasqui LCole;
	private static final String CATEGORIAS_VACIAS = " Existen filas con categorias vacias";
	private static CompleteLinkElementType IdovOwner;
	
	public Grammar_File(CompleteCollection Padre,LoadCollectionChasqui lcole) {
		LCole=lcole;
		{
			Attributo = new CompleteGrammar(NameConstantsChasqui.FILENAME,NameConstantsChasqui.FILENAME,Padre);
			String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
			
			CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			Attributo.getViews().add(Valor);
			Attributo.getViews().add(Valor2);
			Attributo.getViews().add(Valor3);
			
			String VistaOVMeta=new String(NameConstantsChasqui.META);
			
			CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.FILE,VistaOVMeta);

			Attributo.getViews().add(ValorMeta);
			

		}


		{
			IdovOwner = new CompleteLinkElementType(NameConstantsChasqui.IDOV_OWNERNAME, Attributo);
			Attributo.getSons().add(IdovOwner);
			
			String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
			
			CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			IdovOwner.getShows().add(Valor);
			IdovOwner.getShows().add(Valor2);
			IdovOwner.getShows().add(Valor3);
			
			String VistaOVMeta=new String(NameConstantsChasqui.META);
			
			CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.IDOV_OWNER,VistaOVMeta);

			IdovOwner.getShows().add(ValorMeta);
			
		}
		{
			FireRef = new CompleteResourceElementType(NameConstantsChasqui.FILENAME, Attributo);
			Attributo.getSons().add(FireRef);
			
			String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
			
			CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
			CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
			CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
			
			FireRef.getShows().add(Valor);
			FireRef.getShows().add(Valor2);
			FireRef.getShows().add(Valor3);
			
			String VistaOVMeta=new String(NameConstantsChasqui.META);
			
			CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.FILERESOURCE,VistaOVMeta);

			FireRef.getShows().add(ValorMeta);
			
		}
	}

	@Override
	public void ProcessAttributes() {
		
	}

	@Override
	public void ProcessInstances() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT idov,nom_rec,descripcion FROM recursos WHERE tipo!='OV' AND ruta='/';");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String Padre=rs.getObject("idov").toString();
					String Nombre=rs.getObject("nom_rec").toString();
					String Descripcion="";
					if (rs.getObject("descripcion")!=null)
						Descripcion=rs.getObject("descripcion").toString().trim();
					
					if (Padre!=null&&!Padre.isEmpty()&&Nombre!=null&&!Nombre.isEmpty())
						{
						Integer Idov = Integer.parseInt(Padre);	
						StringBuffer SB=new StringBuffer();
						if (LCole.getBaseURLChasqui().isEmpty()||
								(!LCole.getBaseURLChasqui().startsWith("http://")
										&&!LCole.getBaseURLChasqui().startsWith("https://")
										&&!LCole.getBaseURLChasqui().startsWith("ftp://")))
							SB.append("http://");
						SB.append(LCole.getBaseURLChasqui());
						if (!LCole.getBaseURLChasqui().isEmpty()&&!LCole.getBaseURLChasqui().endsWith("/"))
							SB.append("/");
						
						SB.append(Idov+"/"+Nombre.toLowerCase());
						String Path=SB.toString();
						String Path2 = Idov+"/"+Nombre.toLowerCase();
						CompleteFile EFV=new CompleteFile(Path,LCole.getCollection().getChasquiCollection());
							
						CompleteDocuments FileConst= new CompleteDocuments(LCole.getCollection().getChasquiCollection(),Attributo,"",Path);
						
						if (!Descripcion.isEmpty())
							FileConst.setDescriptionText(Descripcion);
							else FileConst.setDescriptionText(Path);
//						FileConst.setIcon(Path);
						
						CompleteResourceElement MR=new CompleteResourceElementFile(this.FireRef, EFV);
						FileConst.getDescription().add(MR);
						
						LCole.getCollection().getChasquiCollection().getSectionValues().add(EFV);
						LCole.getCollection().getChasquiCollection().getEstructuras().add(FileConst);
						LCole.getCollection().getFiles().put(Path2, EFV);
						LCole.getCollection().getFilesC().put(Path2,FileConst);

						}
					else System.err.println(CATEGORIAS_VACIAS);

				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @return the attributo
	 */
	public CompleteGrammar getAttributo() {
		return Attributo;
	}

	/**
	 * @param attributo the attributo to set
	 */
	public void setAttributo(CompleteGrammar attributo) {
		Attributo = attributo;
	}

	/**
	 * @return the idovOwner
	 */
	public static CompleteLinkElementType getIdovOwner() {
		return IdovOwner;
	}

	/**
	 * @param idovOwner the idovOwner to set
	 */
	public static void setIdovOwner(CompleteLinkElementType idovOwner) {
		IdovOwner = idovOwner;
	}

	
}
