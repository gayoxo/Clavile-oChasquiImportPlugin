/**
 * 
 */
package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.StaticFunctionsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * clase que define el parseo de los tipos de recursos en su campo Type
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Resources_TypeRec implements InterfaceChasquiparser{

	
	
	private CompleteTextElementType atributostypes;
	private ArrayList<String> Vocabulario;
	private LoadCollectionChasqui LCole;

	public ElementType_Resources_TypeRec(String typeFile,
			boolean b, CompleteLinkElementType attributo,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributostypes=new CompleteTextElementType(typeFile, attributo);
		
		String VistaMetaType=new String(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.CONTROLED,VistaMetaType);
		atributostypes.getShows().add(MetaType);

		
		Vocabulario= new ArrayList<String>();
		LCole.getCollection().getVocabularios().put(atributostypes, Vocabulario);
	}

	@Override
	public void ProcessAttributes() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct tipoRec FROM recursos;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=null;
					if (rs.getObject("tipoRec")!=null)
						Dato=rs.getObject("tipoRec").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						StaticFunctionsChasqui.addTerm(Vocabulario,Dato);
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void ProcessInstances() {
		
	}
	
	/**
	 * 
	 * @return the ExtendMetaControlled
	 */
	public CompleteTextElementType getExtendMetaControlled() {
		return atributostypes;
	}

}
