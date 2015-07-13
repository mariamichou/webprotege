package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import edu.stanford.bmir.protege.web.server.crud.*;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.bmir.protege.web.server.events.*;
import edu.stanford.bmir.protege.web.server.hierarchy.*;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectModule;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.bmir.protege.web.server.owlapi.change.*;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.server.project.UIConfigurationManager;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.server.metrics.DefaultMetricsCalculators;
import edu.stanford.bmir.protege.web.server.render.DeprecatedEntityCheckerImpl;
import edu.stanford.bmir.protege.web.server.render.EntityIRICheckerImpl;
import edu.stanford.bmir.protege.web.server.render.NullHighlightedEntityChecker;
import edu.stanford.bmir.protege.web.server.watches.*;
import edu.stanford.bmir.protege.web.shared.*;
import edu.stanford.bmir.protege.web.shared.HasDataFactory;
import edu.stanford.bmir.protege.web.shared.axiom.*;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomSubjectProvider;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.object.*;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettings;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.notes.OWLAPINotesManager;
import edu.stanford.bmir.protege.web.server.notes.OWLAPINotesManagerNotesAPIImpl;
import edu.stanford.bmir.protege.web.server.metrics.OWLAPIProjectMetricsManager;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.coode.owlapi.functionalrenderer.OWLFunctionalSyntaxOntologyStorer;
import org.coode.owlapi.owlxml.renderer.OWLXMLOntologyStorer;
import org.coode.owlapi.rdf.rdfxml.RDFXMLOntologyStorer;
import org.protege.owlapi.model.ProtegeOWLOntologyManager;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.AddOntologyAnnotationData;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.*;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.EmptyInMemOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.ParsableOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOntologyStorer;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/03/2012
 */
public class OWLAPIProject implements HasDispose, HasDataFactory, HasContainsEntityInSignature, HasGetEntitiesWithIRI, HasGetEntitiesInSignature, HasGetRevisionSummary, HasApplyChanges, HasLang {


    private final OWLAPIProjectDocumentStore documentStore;

    private final ProjectId projectId;

    private final OWLDataFactory dataFactory;

    private final ProjectAccessManager projectAccessManager;

    private final RenderingManager renderingManager;

    private final EventManager<ProjectEvent<?>> projectEventManager;

    private final OWLOntology ontology;

    private final AssertedClassHierarchyProvider classHierarchyProvider;

    private final OWLObjectPropertyHierarchyProvider objectPropertyHierarchyProvider;

    private final OWLDataPropertyHierarchyProvider dataPropertyHierarchyProvider;

    private final OWLAnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider;

    private final OWLAPISearchManager searchManager;

    private final OWLAPINotesManager notesManager;

    private final RevisionManager changeManager;

    private final ProjectChangesManager projectChangesManager;

    private final WatchedChangesManager watchedChangesManager;

    private final OWLAPIProjectMetricsManager metricsManager;

    private final WatchManager watchManager;


    private final ProjectEntityCrudKitHandlerCache entityCrudKitHandlerCache;

    private final ProjectEntityCrudKitSettingsRepository entityCrudKitSettingsRepository;

    private final Provider<EventTranslatorManager> eventTranslatorManagerProvider;

    private final UIConfigurationManager uiConfigurationManager;

    private final Provider<ManchesterSyntaxFrameParser> manchesterSyntaxFrameParserProvider;


    private final ReadWriteLock projectChangeLock = new ReentrantReadWriteLock();

    private final Lock projectChangeReadLock = projectChangeLock.readLock();

    private final Lock projectChangeWriteLock = projectChangeLock.writeLock();

    private final Lock changeProcesssingLock = new ReentrantLock();

    @Inject
    public OWLAPIProject(OWLAPIProjectDocumentStore documentStore, ProjectId projectId, OWLDataFactory dataFactory, ProjectAccessManager projectAccessManager, RenderingManager renderingManager, EventManager<ProjectEvent<?>> projectEventManager, @RootOntology OWLOntology ontology, AssertedClassHierarchyProvider classHierarchyProvider, OWLObjectPropertyHierarchyProvider objectPropertyHierarchyProvider, OWLDataPropertyHierarchyProvider dataPropertyHierarchyProvider, OWLAnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider, OWLAPISearchManager searchManager, OWLAPINotesManager notesManager, RevisionManager changeManager, ProjectChangesManager projectChangesManager, WatchedChangesManager watchedChangesManager, OWLAPIProjectMetricsManager metricsManager, WatchManager watchManager, ProjectEntityCrudKitHandlerCache entityCrudKitHandlerCache, ProjectEntityCrudKitSettingsRepository entityCrudKitSettingsRepository, Provider<EventTranslatorManager> eventTranslatorManagerProvider, UIConfigurationManager uiConfigurationManager, Provider<ManchesterSyntaxFrameParser> manchesterSyntaxFrameParserProvider) {
        this.documentStore = documentStore;
        this.projectId = projectId;
        this.dataFactory = dataFactory;
        this.projectAccessManager = projectAccessManager;
        this.renderingManager = renderingManager;
        this.projectEventManager = projectEventManager;
        this.ontology = ontology;
        this.classHierarchyProvider = classHierarchyProvider;
        this.objectPropertyHierarchyProvider = objectPropertyHierarchyProvider;
        this.dataPropertyHierarchyProvider = dataPropertyHierarchyProvider;
        this.annotationPropertyHierarchyProvider = annotationPropertyHierarchyProvider;
        this.searchManager = searchManager;
        this.notesManager = notesManager;
        this.changeManager = changeManager;
        this.projectChangesManager = projectChangesManager;
        this.watchedChangesManager = watchedChangesManager;
        this.metricsManager = metricsManager;
        this.watchManager = watchManager;
        this.entityCrudKitHandlerCache = entityCrudKitHandlerCache;
        this.entityCrudKitSettingsRepository = entityCrudKitSettingsRepository;
        this.eventTranslatorManagerProvider = eventTranslatorManagerProvider;
        this.uiConfigurationManager = uiConfigurationManager;
        this.manchesterSyntaxFrameParserProvider = manchesterSyntaxFrameParserProvider;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public String getLang() {
        return "en";
    }

    /**
     * Determines if the specified entity is deprecated in this project.
     *
     * @param entity The entity to test.
     * @return {@code true} if the entity is deprecated in this project, otherwise {@code false}.
     */
    public boolean isDeprecated(OWLEntity entity) {
        if (!getRootOntology().containsAnnotationPropertyInSignature(OWLRDFVocabulary.OWL_DEPRECATED.getIRI(), true)) {
            return false;
        }
        // TODO: Cache
        for (OWLOntology ont : getRootOntology().getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(entity.getIRI())) {
                if (ax.isDeprecatedIRIAssertion()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Optional<RevisionSummary> getRevisionSummary(RevisionNumber revisionNumber) {
        return changeManager.getRevisionSummary(revisionNumber);
    }

    public UIConfigurationManager getUiConfigurationManager() {
        return uiConfigurationManager;
    }

    public EventManager<ProjectEvent<?>> getEventManager() {
        return projectEventManager;
    }

    public ProjectAccessManager getProjectAccessManager() {
        return projectAccessManager;
    }

    public WatchManager getWatchManager() {
        return watchManager;
    }

    public RevisionManager getChangeManager() {
        return changeManager;
    }

    public ProjectChangesManager getProjectChangesManager() {
        return projectChangesManager;
    }

    public WatchedChangesManager getWatchedChangesManager() {
        return watchedChangesManager;
    }

    public AssertedClassHierarchyProvider getClassHierarchyProvider() {
        return classHierarchyProvider;
    }

    public ManchesterSyntaxFrameParser getManchesterSyntaxFrameParser() {
        return manchesterSyntaxFrameParserProvider.get();
    }

    public OWLObjectPropertyHierarchyProvider getObjectPropertyHierarchyProvider() {
        return objectPropertyHierarchyProvider;
    }

    public OWLDataPropertyHierarchyProvider getDataPropertyHierarchyProvider() {
        return dataPropertyHierarchyProvider;
    }

    public OWLAnnotationPropertyHierarchyProvider getAnnotationPropertyHierarchyProvider() {
        return annotationPropertyHierarchyProvider;
    }

    public OWLAPIProjectMetricsManager getMetricsManager() {
        return metricsManager;
    }

    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    public OWLAPISearchManager getSearchManager() {
        return searchManager;
    }

    public OWLOntology getRootOntology() {
        return ontology;
    }

    public RenderingManager getRenderingManager() {
        return renderingManager;
    }

    public OWLAPINotesManager getNotesManager() {
        return notesManager;
    }

    public RevisionNumber getRevisionNumber() {
        try {
            projectChangeReadLock.lock();
            return changeManager.getCurrentRevision();
        } finally {
            projectChangeReadLock.unlock();
        }
    }

    private <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(ChangeSetEntityCrudSession session, UserId userId, String shortName, EntityType<E> entityType) {
        Optional<E> entity = getEntityOfTypeIfPresent(entityType, shortName);
        if (entity.isPresent()) {
            return new OWLEntityCreator<E>(entity.get(), Collections.<OWLOntologyChange>emptyList());
        }
        OntologyChangeList.Builder<E> builder = OntologyChangeList.builder();
        EntityCrudKitHandler<EntityCrudKitSuffixSettings, ChangeSetEntityCrudSession> handler =
                getEntityCrudKitHandler();
        handler.createChangeSetSession();
        E ent = handler.create(session, entityType,
                EntityShortForm.get(shortName),
                getEntityCrudContext(userId),
                builder);
        return new OWLEntityCreator<E>(ent, builder.build().getChanges());

    }

    public void setEntityCrudKitSettings(EntityCrudKitSettings<?> entityCrudKitSettings) {
        ProjectEntityCrudKitSettings projectSettings = new ProjectEntityCrudKitSettings(getProjectId(), entityCrudKitSettings);
        entityCrudKitSettingsRepository.save(projectSettings);
    }


    @SuppressWarnings("unchecked")
    public <S extends EntityCrudKitSuffixSettings, C extends ChangeSetEntityCrudSession> EntityCrudKitHandler<S, C> getEntityCrudKitHandler() {
        return (EntityCrudKitHandler<S, C>) entityCrudKitHandlerCache.getHandler();
    }

    public EntityCrudContext getEntityCrudContext(UserId userId) {
        PrefixedNameExpander expander = PrefixedNameExpander.builder().withNamespaces(Namespaces.values()).build();
        return new EntityCrudContext(userId, getRootOntology(), getDataFactory(), expander);
    }

    @SuppressWarnings("unchecked")
    private <E extends OWLEntity> Optional<E> getEntityOfTypeIfPresent(EntityType<E> entityType, String shortName) {
        for (OWLEntity entity : renderingManager.getEntities(shortName)) {
            if (entity.isType(entityType)) {
                return Optional.of((E) entity);
            }
        }
        return Optional.absent();
    }

    /**
     * Applies a list of changes to ontologies in this project.
     *
     * @param userId            The userId of the user applying the changes. Not {@code null}.
     * @param changes           The list of changes to be applied.  Not {@code null}.
     * @param changeDescription A description of the changes. Not {@code null}.
     * @return A {@link ChangeApplicationResult} that describes the changes which took place an any renaminings.
     * @throws NullPointerException if any parameters are {@code null}.
     * @deprecated Use {@link #applyChanges(edu.stanford.bmir.protege.web.shared.user.UserId,
     * edu.stanford.bmir.protege.web.server.change.ChangeListGenerator, ChangeDescriptionGenerator)}
     */
    @Deprecated
    public ChangeApplicationResult<?> applyChanges(UserId userId, List<OWLOntologyChange> changes, String changeDescription) {
        return applyChanges(userId, FixedChangeListGenerator.get(changes), FixedMessageChangeDescriptionGenerator.get(changeDescription));
    }

    /**
     * Applies ontology changes to the ontologies contained within a project.
     *
     * @param userId                     The userId of the user applying the changes.  Not {@code null}.
     * @param changeListGenerator        A generator which creates a list of changes (based on the state of the project at
     *                                   the time of change application).  The idea behind passing in a change generator is that the list of changes to
     *                                   be applied can be created based on the state of the project immediately before they are applied.  This is
     *                                   necessary where the changes depend on the structure/state of the ontology.  This method guarantees that no third
     *                                   party
     *                                   ontology changes will take place between the {@link ChangeListGenerator#generateChanges(OWLAPIProject,
     *                                   edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext)}
     *                                   method being called and the changes being applied.
     * @param changeDescriptionGenerator A generator that describes the changes that took place.
     * @return A {@link ChangeApplicationResult} that describes the changes which took place an any renaminings.
     * @throws NullPointerException      if any parameters are {@code null}.
     * @throws PermissionDeniedException if the user identified by {@code userId} does not have permssion to write to
     *                                   ontologies in this project.
     */
    @Override
    public <R> ChangeApplicationResult<R> applyChanges(final UserId userId, final ChangeListGenerator<R> changeListGenerator, final ChangeDescriptionGenerator<R> changeDescriptionGenerator) throws PermissionDeniedException {
        checkNotNull(userId);
        checkNotNull(changeListGenerator);
        checkNotNull(changeDescriptionGenerator);

        if (!getPermissionsManager().hasWriteAccess(userId)) {
            throw new PermissionDeniedException("You do not have permission to write to ontologies in this project");
        }

        final Set<OWLEntity> changeSignature = new HashSet<OWLEntity>();
        final List<OWLOntologyChange> appliedChanges;
        final ChangeApplicationResult<R> finalResult;


        // The following must take into consideration fresh entity IRIs.  Entity IRIs are minted on the server, so
        // ontology changes may contain fresh entity IRIs as place holders. We need to make sure these get replaced
        // with true entity IRIs
        try {
            // Compute the changes that need to take place.  We don't allow any other writes here because the
            // generation of the changes may depend upon the state of the project
            changeProcesssingLock.lock();

            final ChangeGenerationContext context = new ChangeGenerationContext(userId);
            OntologyChangeList<R> gen = changeListGenerator.generateChanges(this, context);

            // We have our changes

            List<OWLOntologyChange> changes = gen.getChanges();

            // We coin fresh entities for places where tmp: is the scheme - the name for the entity comes from
            // the fragment
            final Map<IRI, IRI> iriRenameMap = new HashMap<IRI, IRI>();

            final ChangeSetEntityCrudSession session = getEntityCrudKitHandler().createChangeSetSession();
            Set<OWLOntologyChange> changesToRename = new HashSet<OWLOntologyChange>();
            List<OWLOntologyChange> freshEntityChanges = new ArrayList<OWLOntologyChange>();
            for (OWLOntologyChange change : changes) {
                for (OWLEntity entity : change.getSignature()) {
                    if (DataFactory.isFreshEntity(entity)) {
                        changesToRename.add(change);
                        IRI currentIRI = entity.getIRI();
                        if (!iriRenameMap.containsKey(currentIRI)) {
                            String shortName = DataFactory.getFreshEntityShortName(entity);
                            OWLEntityCreator<? extends OWLEntity> creator = getEntityCreator(session, userId, shortName, (EntityType<? extends OWLEntity>) entity.getEntityType());
                            freshEntityChanges.addAll(creator.getChanges());
                            IRI replacementIRI = creator.getEntity().getIRI();
                            iriRenameMap.put(currentIRI, replacementIRI);
                        }
                    }
                }
            }


            List<OWLOntologyChange> allChangesIncludingRenames = new ArrayList<>();
            final OWLObjectDuplicator duplicator = new OWLObjectDuplicator(dataFactory, iriRenameMap);
            for (OWLOntologyChange change : changes) {
                if (changesToRename.contains(change)) {
                    OWLOntologyChange replacementChange = getRenamedChange(change, duplicator);
                    allChangesIncludingRenames.add(replacementChange);
                }
                else {
                    allChangesIncludingRenames.add(change);
                }
            }

            allChangesIncludingRenames.addAll(freshEntityChanges);

            List<OWLOntologyChange> minimisedChanges = getMinimisedChanges(allChangesIncludingRenames);

            for (OWLOntologyChange change : minimisedChanges) {
                changeSignature.addAll(change.getSignature());
            }

            final EventTranslatorManager eventTranslatorManager = eventTranslatorManagerProvider.get();
            eventTranslatorManager.prepareForOntologyChanges(minimisedChanges);

            // Now we do the actual changing, so we lock the project here.  No writes or reads can take place whilst
            // we apply the changes
            try {
                projectChangeWriteLock.lock();
                OWLAPIProjectOWLOntologyManager manager = ((OWLAPIProjectOWLOntologyManager) getRootOntology().getOWLOntologyManager());
                appliedChanges = manager.getDelegate().applyChanges(minimisedChanges);
                final RenameMap renameMap = new RenameMap(iriRenameMap);
                Optional<R> renamedResult = getRenamedResult(changeListGenerator, gen.getResult(), renameMap);
                finalResult = new ChangeApplicationResult<R>(renamedResult, appliedChanges, renameMap);
                if (!appliedChanges.isEmpty()) {
                    logAndBroadcastAppliedChanges(userId, finalResult, changeDescriptionGenerator);
                }
            } finally {
                // Release for reads
                projectChangeWriteLock.unlock();
            }


            if (!(changeListGenerator instanceof SilentChangeListGenerator)) {
                List<ProjectEvent<?>> highLevelEvents = new ArrayList<>();
                eventTranslatorManager.translateOntologyChanges(appliedChanges, highLevelEvents);
                if (changeListGenerator instanceof HasHighLevelEvents) {
                    highLevelEvents.addAll(((HasHighLevelEvents) changeListGenerator).getHighLevelEvents());
                }
                projectEventManager.postEvents(highLevelEvents);
            }
        } finally {
            changeProcesssingLock.unlock();
        }

        return finalResult;


    }

    private List<OWLOntologyChange> getMinimisedChanges(List<OWLOntologyChange> allChangesIncludingRenames) {
        Set<OWLAxiom> axiomsToAdd = new HashSet<OWLAxiom>();
        Set<OWLAxiom> axiomsToRemove = new HashSet<OWLAxiom>();
        for (OWLOntologyChange change : allChangesIncludingRenames) {
            if (change.isAddAxiom()) {
                axiomsToAdd.add(change.getAxiom());
                axiomsToRemove.remove(change.getAxiom());
            }
            else if (change.isRemoveAxiom()) {
                axiomsToRemove.add(change.getAxiom());
                axiomsToAdd.remove(change.getAxiom());
            }
        }

        // Minimise changes
        List<OWLOntologyChange> minimisedChanges = new ArrayList<OWLOntologyChange>();
        for (OWLOntologyChange change : allChangesIncludingRenames) {
            if (change.isAddAxiom()) {
                if (axiomsToAdd.contains(change.getAxiom())) {
                    minimisedChanges.add(change);
                }
            }
            else if (change.isRemoveAxiom()) {
                if (axiomsToRemove.contains(change.getAxiom())) {
                    minimisedChanges.add(change);
                }
            }
            else {
                minimisedChanges.add(change);
            }
        }
        return minimisedChanges;
    }

    /**
     * Renames a result if it is present.
     *
     * @param result    The result to process.
     * @param renameMap The rename map.
     * @param <R>       The type of result.
     * @return The renamed (or untouched if no rename was necessary) result.
     */
    private <R> Optional<R> getRenamedResult(ChangeListGenerator<R> changeListGenerator, Optional<R> result, RenameMap renameMap) {
        Optional<R> renamedResult;
        if (result.isPresent()) {
            R actualResult = result.get();
            renamedResult = Optional.of(changeListGenerator.getRenamedResult(actualResult, renameMap));
        }
        else {
            renamedResult = result;
        }
        return renamedResult;
    }


    private <R> void logAndBroadcastAppliedChanges(UserId userId, ChangeApplicationResult<R> finalResult, ChangeDescriptionGenerator<R> changeDescriptionGenerator) {
        // Generate a description for the changes that were actually applied
        String changeDescription = changeDescriptionGenerator.generateChangeDescription(finalResult);
        // Log the changes
        List<OWLOntologyChangeRecord> changeRecords = new ArrayList<>();
        for (OWLOntologyChange change : finalResult.getChangeList()) {
            changeRecords.add(change.getChangeRecord());
        }
        changeManager.addRevision(userId, changeRecords, changeDescription);

        // TODO: THis list of "listeners" should be injected
        List<OWLOntologyChange> changes = finalResult.getChangeList();
        documentStore.saveOntologyChanges(changes);

        classHierarchyProvider.handleChanges(changes);
        objectPropertyHierarchyProvider.handleChanges(changes);
        dataPropertyHierarchyProvider.handleChanges(changes);
        annotationPropertyHierarchyProvider.handleChanges(changes);
        metricsManager.handleOntologyChanges(changes);
    }


    /**
     * Gets an ontology change which is a copy of an existing ontology change except for IRIs that are renamed.
     * Renamings
     * are specified by a rename map.
     *
     * @param change     The change to copy.
     * @param duplicator A duplicator used to rename IRIs
     * @return The ontology change with the renamings.
     */
    private OWLOntologyChange getRenamedChange(OWLOntologyChange change, final OWLObjectDuplicator duplicator) {
        return change.accept(new OWLOntologyChangeVisitorAdapterEx<OWLOntologyChange>() {

            @SuppressWarnings("unchecked")
            private <T extends OWLObject> T duplicate(T ax) {
                OWLObject object = duplicator.duplicateObject(ax);
                return (T) object;
            }

            @Override
            public OWLOntologyChange visit(RemoveAxiom change) {
                return new RemoveAxiom(change.getOntology(), duplicate(change.getAxiom()));
            }

            @Override
            public OWLOntologyChange visit(SetOntologyID change) {
                return change;
            }

            @Override
            public OWLOntologyChange visit(AddAxiom change) {
                return new AddAxiom(change.getOntology(), duplicate(change.getAxiom()));
            }

            @Override
            public OWLOntologyChange visit(AddImport change) {
                return change;
            }

            @Override
            public OWLOntologyChange visit(RemoveImport change) {
                return change;
            }

            @Override
            public OWLOntologyChange visit(AddOntologyAnnotation change) {
                return new AddOntologyAnnotation(change.getOntology(), duplicate(change.getAnnotation()));
            }

            @Override
            public OWLOntologyChange visit(RemoveOntologyAnnotation change) {
                return new RemoveOntologyAnnotation(change.getOntology(), duplicate(change.getAnnotation()));
            }
        });
    }

    @Override
    public boolean containsEntityInSignature(OWLEntity entity) {
        return getRootOntology().containsEntityInSignature(entity, true);
    }

    @Override
    public Set<OWLEntity> getEntitiesWithIRI(IRI iri) {
        return getRootOntology().getEntitiesInSignature(iri, true);
    }

    @Override
    public Set<OWLEntity> getEntitiesInSignature(IRI entityIRI) {
        return getRootOntology().getEntitiesInSignature(entityIRI, true);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AxiomSubjectProvider getAxiomSubjectProvider() {
        Comparator<OWLObject> comparator = getOWLObjectComparator();
        return new AxiomSubjectProvider(
                new OWLClassExpressionSelector(comparator),
                new OWLObjectPropertyExpressionSelector(comparator),
                new OWLDataPropertyExpressionSelector(comparator),
                new OWLIndividualSelector(comparator),
                new SWRLAtomSelector(comparator)
        );
    }

    public Comparator<OWLObject> getOWLObjectComparator() {
        return new OWLObjectComparatorImpl(
                getRenderingManager()
        );
    }

    private OWLObjectRenderer getOWLObjectRenderer() {
        return new OWLObjectRenderer() {
            @Override
            public void setShortFormProvider(ShortFormProvider shortFormProvider) {

            }

            @Override
            public String render(OWLObject object) {
                return renderingManager.getHTMLBrowserText(object);
            }
        };
    }

    public Comparator<OWLAxiom> getAxiomComparator() {
        return new AxiomComparatorImpl(
                new AxiomBySubjectComparator(
                        getAxiomSubjectProvider(),
                        getOWLObjectComparator()
                ),
                new AxiomByTypeComparator(
                        DefaultAxiomTypeOrdering.get()
                ),
                new AxiomByRenderingComparator(
                        getOWLObjectRenderer()
                )
        );
    }

    private final ProjectPermissionsManager permissionsManager = new DummyPermissionsManager();

    private ProjectPermissionsManager getPermissionsManager() {
        return permissionsManager;
    }


    private static class DummyPermissionsManager implements ProjectPermissionsManager {

        @Override
        public boolean hasReadAccess(UserId userId) {
            return true;
        }

        @Override
        public boolean hasWriteAccess(UserId userId) {
            return true;
        }

        @Override
        public boolean hasCommentAccess(UserId userId) {
            return true;
        }
    }


    @Override
    public void dispose() {
        projectEventManager.dispose();
        classHierarchyProvider.dispose();
        objectPropertyHierarchyProvider.dispose();
        dataPropertyHierarchyProvider.dispose();
        annotationPropertyHierarchyProvider.dispose();
        projectAccessManager.dispose();
    }

}
