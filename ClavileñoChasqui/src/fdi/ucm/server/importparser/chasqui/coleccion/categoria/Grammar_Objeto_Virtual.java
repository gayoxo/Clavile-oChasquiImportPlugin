package fdi.ucm.server.importparser.chasqui.coleccion.categoria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.StaticFunctionsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.ElementType_IDOV;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.ElementType_Resources;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteFile;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteIterator;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;

/**
 * Clase que implementa la creacion de la seccion de objetos virtuales
 * @author Joaquin Gayoso-Cabada
 *
 */
public class Grammar_Objeto_Virtual implements InterfaceChasquiparser{

	private HashMap<Integer, CompleteDocuments> ObjetoVirtual;
	private CompleteGrammar MetaAtt;
	private ElementType_IDOV Idov;
	private ElementType_Resources EF;
	private LoadCollectionChasqui LCole;
	
	/**
	 * Constructor por defecto de la clase
	 * @param chasquiImplementationExtendCollection 
	 */
	public Grammar_Objeto_Virtual(CompleteCollection Padre,LoadCollectionChasqui lcole) {
		LCole=lcole;
		ObjetoVirtual=new HashMap<Integer, CompleteDocuments>();
		MetaAtt=new CompleteGrammar(NameConstantsChasqui.VIRTUAL_OBJECTNAME, NameConstantsChasqui.VIRTUAL_OBJECTNAME,Padre);
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		MetaAtt.getViews().add(Valor);
		MetaAtt.getViews().add(Valor2);
		MetaAtt.getViews().add(Valor3);
		

		String VistaOVMeta=new String(NameConstantsChasqui.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.VIRTUAL_OBJECT,VistaOVMeta);
		
		MetaAtt.getViews().add(ValorMeta);
		
		
		String VistaOVOda=new String(NameConstantsChasqui.ODA);
		
		CompleteOperationalValueType ValorOda=new CompleteOperationalValueType(NameConstantsChasqui.PUBLIC,Boolean.toString(true),VistaOVOda);

		CompleteOperationalValueType ValorOda2=new CompleteOperationalValueType(NameConstantsChasqui.PRIVATE,Boolean.toString(false),VistaOVOda);

		MetaAtt.getViews().add(ValorOda);
		MetaAtt.getViews().add(ValorOda2);
		
		
	}
	
	@Override
	public void ProcessAttributes() {
		
		
		
		Idov=new ElementType_IDOV(NameConstantsChasqui.IDOVNAME,false,MetaAtt,true,LCole);
		Idov.ProcessAttributes();
		MetaAtt.getSons().add(Idov.getAttributo());
		
	
		CompleteIterator MultivaluedFile = new CompleteIterator(MetaAtt);
		MetaAtt.getSons().add(MultivaluedFile);
		
		 EF = new ElementType_Resources(MultivaluedFile,LCole);
		EF.ProcessAttributes();
		MultivaluedFile.getSons().add(EF.getAttributo());
		
		
		
			
	}

	
	@Override
	public void ProcessInstances() {
		process_idovs();
		LCole.getCollection().setObjetoVirtual(ObjetoVirtual);
		Idov.ProcessInstances();

		EF.ProcessInstances();
		
		
		

		for (Entry<Integer, CompleteDocuments> elemento : ObjetoVirtual.entrySet()) {
			CompleteFile icono=calculasuIcono(elemento.getValue());
			if (icono!=null)
				elemento.getValue().setIcon(icono.getPath());

			
		}
		
	}
	

private void process_idovs() {
	try {
		ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT idov FROM objeto_virtual;");
		if (rs!=null) 
		{
			while (rs.next()) {
				
				String Dato=rs.getObject("idov").toString();
				if (Dato!=null&&!Dato.isEmpty())
					{
					int Idov=Integer.parseInt(Dato);
					CompleteCollection C=LCole.getCollection().getChasquiCollection();
					CompleteDocuments sectionValue = new CompleteDocuments(C,"","");
					C.getEstructuras().add(sectionValue);
					ObjetoVirtual.put(Idov, sectionValue);
					}
				else System.err.println("ErrorIdovNotFound");
			}
		rs.close();
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
		
	
}


/**
 * Funcion que retorna el calculo del icono para insertarlo como attributo en el objeto virtual
 * @param completeDocuments identificador a evaluar
 * @return el section valuye file que lo define
 */
private CompleteFile calculasuIcono(CompleteDocuments completeDocuments) {

	Integer Idov=Integer.parseInt(((CompleteTextElement)StaticFunctionsChasqui.FindDescAtrib(completeDocuments.getDescription(), NameConstantsChasqui.IDOVNAME)).getValue());
	
	try {
		ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT id,nom_rec,ruta,tipo FROM recursos WHERE idov="+Idov+" AND (tipo=\"jpg\" OR tipo=\"jpge\" OR tipo=\"gif\" OR tipo=\"png\") ORDER BY id;");
		if (rs!=null) 
		{
			while (rs.next()) {

				String Dato2=rs.getObject("nom_rec").toString();
				String Dato=rs.getObject("ruta").toString();
				if (Dato!=null&&!Dato.isEmpty()&&Dato2!=null&&!Dato2.isEmpty())
					{
						String IdovRef=Dato.substring(0, Dato.length()-1);
						if (IdovRef.isEmpty())
							return LCole.getCollection().getFiles().get(Idov+"/"+Dato2.toLowerCase());
						else return LCole.getCollection().getFiles().get(IdovRef+"/"+Dato2.toLowerCase());
					}
				else System.err.println("ErrorIdovNotFound");
			}
		rs.close();
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	return null;
	

}




/**
 * @return the objetoVirtual
 */
public HashMap<Integer, CompleteDocuments> getObjetoVirtual() {
	return ObjetoVirtual;
}

/**
 * @param objetoVirtual the objetoVirtual to set
 */
public void setObjetoVirtual(HashMap<Integer, CompleteDocuments> objetoVirtual) {
	ObjetoVirtual = objetoVirtual;
}





/**
 * @return the meta
 */
public CompleteGrammar getMeta() {
	return MetaAtt;
}

/**
 * @param elementType the meta to set
 */
public void setMeta(CompleteGrammar elementType) {
	MetaAtt = elementType;
}



	
	
}
