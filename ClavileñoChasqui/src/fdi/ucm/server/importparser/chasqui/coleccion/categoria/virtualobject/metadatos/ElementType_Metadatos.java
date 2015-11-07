package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos.catalogo.ElementType_Catalogos;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos.taxonomia.ElementType_Taxonomias;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteStructure;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;


/**
 * Clase que define el parseado de los metadatos
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Metadatos implements InterfaceChasquiparser{



	private CompleteStructure atributometadato;
	private LoadCollectionChasqui LCole;

	/**
	 * Constructor con parametros
	 * @param name Nombre de la categoria
	 * @param browseable si es navegable
	 * @param father padre de la categoria
	 */
	public ElementType_Metadatos(String name, boolean browseable,
			CompleteGrammar father,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributometadato=new CompleteStructure(name,father);
		
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		atributometadato.getShows().add(Valor);
		atributometadato.getShows().add(Valor2);
		atributometadato.getShows().add(Valor3);
		
		String VistaOVMeta=new String(NameConstantsChasqui.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.METADATOS,VistaOVMeta);

		atributometadato.getShows().add(ValorMeta);
		
		
	}
	
	@Override
	public void ProcessInstances() {
		
		
	}

	
	@Override
	public void ProcessAttributes() {
		ContextProcess();
		KeywordProcess();
		DescripcionProcess();
		StatusProcess();
		ContributionProcess();
		
		//Catalogprocess()
		ElementType_Catalogos AMCatalogo= new ElementType_Catalogos("Catalogos",false, atributometadato,LCole);
		atributometadato.getSons().add(AMCatalogo.getExtendMeta());
		AMCatalogo.ProcessAttributes();
		
		//taxonProcess()
		ElementType_Taxonomias AMTaxonimias= new ElementType_Taxonomias("Taxonomias",false, atributometadato,LCole);
		atributometadato.getSons().add(AMTaxonimias.getExtendMeta());
		AMTaxonimias.ProcessAttributes();
		
	}
	
	/**
	 * Procesa los contribuidores.
	 */
	private void ContributionProcess() {

		CompleteIterator Estrella=new CompleteIterator(atributometadato);
		ElementType_Metadatos_Contribucion AMcontribucion= new ElementType_Metadatos_Contribucion("Contribucion",false, Estrella,LCole);
		Estrella.getSons().add(AMcontribucion.getExtendMeta());
		atributometadato.getSons().add(Estrella);
		AMcontribucion.ProcessAttributes();
		AMcontribucion.ProcessInstances();
				
	}



	/**
	 * Funcion que procesa los status.
	 */
	private void StatusProcess() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("Select MAX(sumar) AS resultado FROM (SELECT idov, count(*) as sumar FROM metadatos WHERE ruta='/manifest/metadata/lom/lifecycle/status/value/langstring' Group BY idov) AS A;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=rs.getObject("resultado").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						Integer count=Integer.parseInt(Dato);
						ElementType_Metadatos_Status AMstatus=new ElementType_Metadatos_Status("Estatus", false, atributometadato,LCole);
						atributometadato.getSons().add(AMstatus.getExtendMetaControlled());
						ElementType_Metadatos_Status.setStatusElements(new ArrayList<CompleteTextElementType>());
						ElementType_Metadatos_Status.getStatusElements().add(AMstatus.getExtendMetaControlled());
						AMstatus.ProcessAttributes();
						ArrayList<String> forAll=AMstatus.getVocabulario();
						for (int i = 1; i < count; i++) {
							AMstatus=new ElementType_Metadatos_Status("Estatus", false, atributometadato,LCole);
							atributometadato.getSons().add(AMstatus.getExtendMetaControlled());
							LCole.getCollection().getVocabularios().put(AMstatus.getExtendMetaControlled(), forAll);
							ElementType_Metadatos_Status.getStatusElements().add(AMstatus.getExtendMetaControlled());
						}
						AMstatus.ProcessInstances();
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}



	/**
	 * Funcion que procesa las descripciones.
	 */
	private void DescripcionProcess() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("Select MAX(sumar) AS resultado FROM (SELECT idov, count(*) AS sumar FROM metadatos where ruta='/manifest/metadata/lom/general/description/langstring' Group By idov) AS A;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=rs.getObject("resultado").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						Integer count=Integer.parseInt(Dato);
						ElementType_Metadatos_Description AMdescripcion= new ElementType_Metadatos_Description("Descripcion", false, atributometadato,LCole);
						atributometadato.getSons().add(AMdescripcion.getExtendMetaText());
						ElementType_Metadatos_Description.setDescriptionElements(new ArrayList<CompleteTextElementType>());
						AMdescripcion.ProcessAttributes();
						ElementType_Metadatos_Description.getDescriptionElements().add(AMdescripcion.getExtendMetaText());
						for (int i = 1; i < count; i++) {
							AMdescripcion= new ElementType_Metadatos_Description("Descripcion", false, atributometadato,LCole);
							atributometadato.getSons().add(AMdescripcion.getExtendMetaText());
							ElementType_Metadatos_Description.getDescriptionElements().add(AMdescripcion.getExtendMetaText());
						}
						AMdescripcion.ProcessInstances();
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Funcion que procesa las palabras clave
	 */
	private void KeywordProcess() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("Select MAX(sumar) AS resultado FROM (SELECT idov,count(*) as sumar FROM metadatos Where ruta = '/manifest/metadata/lom/general/coverage/langstring' GROUP BY idov) AS A;");
			if (rs!=null) 
			{
				while (rs.next()) {
					String Dato=rs.getObject("resultado").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						Integer count=Integer.parseInt(Dato);
						ElementType_Metadatos_Keyword AMkeyword= new ElementType_Metadatos_Keyword("Palablas Clave", false, atributometadato,LCole);
						ElementType_Metadatos_Keyword.setKeywordElements(new ArrayList<CompleteTextElementType>());
						atributometadato.getSons().add(AMkeyword.getExtendMetaControlled());
						ElementType_Metadatos_Keyword.getKeywordElements().add(AMkeyword.getExtendMetaControlled());
						AMkeyword.ProcessAttributes();
						ArrayList<String> forAll=AMkeyword.getVocabulario();
						for (int i = 1; i < count; i++) {
							AMkeyword= new ElementType_Metadatos_Keyword("Palablas Clave", false, atributometadato,LCole);
							atributometadato.getSons().add(AMkeyword.getExtendMetaControlled());
							LCole.getCollection().getVocabularios().put(AMkeyword.getExtendMetaControlled(), forAll);
							ElementType_Metadatos_Keyword.getKeywordElements().add(AMkeyword.getExtendMetaControlled());
						}
						AMkeyword.ProcessInstances();
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Funcion que procesa el contexto
	 */
	private void ContextProcess() {
		
		CompleteIterator Estrella=new CompleteIterator(atributometadato);
		ElementType_Metadatos_Contexto AMcontenido=new ElementType_Metadatos_Contexto("Contexto", false, Estrella,LCole);
		Estrella.getSons().add(AMcontenido.getExtendMetaControlled());
		atributometadato.getSons().add(Estrella);
		AMcontenido.ProcessAttributes();
		AMcontenido.ProcessInstances();

			
	}



	/**
	 * 
	 * @return ExtendMeta
	 */
	public CompleteStructure getMeta() {
		return atributometadato;
	}



	
}
