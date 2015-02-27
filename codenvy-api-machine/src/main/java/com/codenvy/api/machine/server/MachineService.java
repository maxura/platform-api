/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.api.machine.server;

import com.codenvy.api.core.ConflictException;
import com.codenvy.api.core.ForbiddenException;
import com.codenvy.api.core.NotFoundException;
import com.codenvy.api.core.ServerException;
import com.codenvy.api.core.util.LineConsumer;
import com.codenvy.api.core.util.WebsocketLineConsumer;
import com.codenvy.api.machine.shared.MachineState;
import com.codenvy.api.machine.shared.ProjectBinding;
import com.codenvy.api.machine.shared.dto.CreateMachineFromRecipe;
import com.codenvy.api.machine.shared.dto.CreateMachineFromSnapshot;
import com.codenvy.api.machine.shared.dto.MachineDescriptor;
import com.codenvy.api.machine.shared.dto.CommandDescriptor;
import com.codenvy.api.machine.shared.dto.ProcessDescriptor;
import com.codenvy.api.machine.shared.dto.NewSnapshotDescriptor;
import com.codenvy.api.machine.shared.dto.ProjectBindingDescriptor;
import com.codenvy.api.machine.shared.dto.SnapshotDescriptor;
import com.codenvy.api.workspace.server.dao.Member;
import com.codenvy.api.workspace.server.dao.MemberDao;
import com.codenvy.commons.env.EnvironmentContext;
import com.codenvy.dto.server.DtoFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Machine API
 *
 * @author Alexander Garagatyi
 */
@Path("/machine")
public class MachineService {
    private MachineManager machineManager;
    private DtoFactory     dtoFactory;
    private MemberDao      memberDao;

    @Inject
    public MachineService(MachineManager machineManager, MemberDao memberDao) {
        this.machineManager = machineManager;
        this.memberDao = memberDao;
        this.dtoFactory = DtoFactory.getInstance();
    }

    @Path("/recipe")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public MachineDescriptor createMachineFromRecipe(final CreateMachineFromRecipe createMachineRequest)
            throws ServerException, ForbiddenException, NotFoundException {
        requiredNotNull(createMachineRequest.getRecipeDescriptor(), "Machine type");
        requiredNotNull(createMachineRequest.getWorkspaceId(), "Workspace id");
        requiredNotNull(createMachineRequest.getRecipeDescriptor(), "Recipe descriptor");
        requiredNotNull(createMachineRequest.getRecipeDescriptor().getScript(), "Recipe script");
        requiredNotNull(createMachineRequest.getRecipeDescriptor().getType(), "Recipe type");

        checkCurrentUserPermissionsForWorkspace(createMachineRequest.getWorkspaceId());

        final LineConsumer lineConsumer = getLineConsumer(createMachineRequest.getOutputChannel());

        final MachineImpl machine = machineManager.create(createMachineRequest.getType(),
                                                          RecipeImpl.fromDescriptor(createMachineRequest.getRecipeDescriptor()),
                                                          createMachineRequest.getWorkspaceId(),
                                                          EnvironmentContext.getCurrent().getUser().getId(),
                                                          lineConsumer);

        // TODO displayName? machine description?
        return toDescriptor(machine.getId(),
                            machine.getType(),
                            machine.getState(),
                            machine.getOwner(),
                            machine.getWorkspaceId(),
                            Collections.<ProjectBinding>emptySet());
    }

    @Path("/snapshot")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public MachineDescriptor createMachineFromSnapshot(CreateMachineFromSnapshot createMachineRequest)
            throws ForbiddenException, NotFoundException, ServerException {
        requiredNotNull(createMachineRequest.getSnapshotId(), "Snapshot id");
        final Snapshot snapshot = machineManager.getSnapshot(createMachineRequest.getSnapshotId());
        checkCurrentUserPermissionsForSnapshot(snapshot);
        checkCurrentUserPermissionsForWorkspace(snapshot.getWorkspaceId());

        final LineConsumer lineConsumer = getLineConsumer(createMachineRequest.getOutputChannel());

        final MachineImpl machine = machineManager.create(createMachineRequest.getSnapshotId(),
                                                          EnvironmentContext.getCurrent().getUser().getId(),
                                                          lineConsumer);

        return toDescriptor(machine.getId(),
                            machine.getType(),
                            machine.getState(),
                            machine.getOwner(),
                            machine.getWorkspaceId(),
                            Collections.<ProjectBinding>emptySet());
    }

    @Path("/{machineId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public MachineDescriptor getMachineById(@PathParam("machineId") String machineId)
            throws ServerException, ForbiddenException, NotFoundException {
        final MachineImpl machine = machineManager.getMachine(machineId);

        checkCurrentUserPermissionsForMachine(machine.getOwner());

        return toDescriptor(machineId,
                            machine.getType(),
                            machine.getState(),
                            machine.getOwner(),
                            machine.getWorkspaceId(),
                            machine.getProjectBindings());
    }

    @Path("/workspace/{ws-id}/project/{path:.*}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public List<MachineDescriptor> getMachines(@PathParam("ws-id") String workspaceId,
                                               @PathParam("path") String path)
            throws ServerException, ForbiddenException {
        requiredNotNull(workspaceId, "Workspace parameter");

        final String userId = EnvironmentContext.getCurrent().getUser().getId();
        final List<MachineImpl> machines = machineManager.getMachines(userId, workspaceId, new ProjectBindingImpl().withPath(path));

        final List<MachineDescriptor> machinesDescriptors = new LinkedList<>();
        for (MachineImpl machine : machines) {
            machinesDescriptors.add(toDescriptor(machine.getId(),
                                                 machine.getType(),
                                                 machine.getState(),
                                                 machine.getOwner(),
                                                 machine.getWorkspaceId(),
                                                 machine.getProjectBindings()));
        }

        return machinesDescriptors;
    }

    @Path("/{machineId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public void destroyMachine(@PathParam("machineId") String machineId) throws NotFoundException, ServerException, ForbiddenException {
        checkCurrentUserPermissionsForMachine(machineManager.getMachine(machineId).getOwner());

        machineManager.destroy(machineId);
    }

    @Path("/snapshot/workspace/{ws-id}/project/{path:.*}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public List<SnapshotDescriptor> getSnapshots(@PathParam("ws-id") String workspaceId, @PathParam("path") String path)
            throws ServerException {
        final List<Snapshot> snapshots = machineManager.getSnapshots(EnvironmentContext.getCurrent().getUser().getId(),
                                                                     workspaceId,
                                                                     new ProjectBindingImpl().withPath(path));

        final List<SnapshotDescriptor> snapshotDescriptors = new LinkedList<>();
        for (Snapshot snapshot : snapshots) {
            snapshotDescriptors.add(toDescriptor(snapshot));
        }

        return snapshotDescriptors;
    }

    @Path("/{machineId}/snapshot")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public void saveSnapshot(@PathParam("machineId") String machineId, NewSnapshotDescriptor newSnapshotDescriptor)
            throws NotFoundException, ServerException, ForbiddenException {
        checkCurrentUserPermissionsForMachine(machineManager.getMachine(machineId).getOwner());

        machineManager.save(machineId, EnvironmentContext.getCurrent().getUser().getId(), newSnapshotDescriptor.getDescription());
    }

    @Path("/snapshot/{snapshotId}")
    @DELETE
    @RolesAllowed("user")
    public void removeSnapshot(@PathParam("snapshotId") String snapshotId) throws ForbiddenException, NotFoundException, ServerException {
        checkCurrentUserPermissionsForSnapshot(machineManager.getSnapshot(snapshotId));

        machineManager.removeSnapshot(snapshotId);
    }

    @Path("/{machineId}/process")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public void executeCommandInMachine(@PathParam("machineId") String machineId, final CommandDescriptor command)
            throws NotFoundException, ServerException, ForbiddenException {
        checkCurrentUserPermissionsForMachine(machineManager.getMachine(machineId).getOwner());

        final LineConsumer lineConsumer = getLineConsumer(command.getOutputChannel());

        machineManager.exec(machineId, command, lineConsumer);
    }

    @Path("/{machineId}/process")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public List<ProcessDescriptor> getProcesses(@PathParam("machineId") String machineId)
            throws NotFoundException, ServerException, ForbiddenException {
        checkCurrentUserPermissionsForMachine(machineManager.getMachine(machineId).getOwner());

        final List<ProcessDescriptor> processesDescriptors = new LinkedList<>();
        for (ProcessImpl process : machineManager.getProcesses(machineId)) {
            processesDescriptors.add(toDescriptor(process.getPid(), process.getCommandLine(), process.isAlive()));
        }

        return processesDescriptors;
    }

    @Path("/{machineId}/process/{processId}")
    @DELETE
    @RolesAllowed("user")
    public void stopProcess(@PathParam("machineId") String machineId,
                            @PathParam("processId") int processId)
            throws NotFoundException, ForbiddenException, ServerException {
        checkCurrentUserPermissionsForMachine(machineManager.getMachine(machineId).getOwner());

        machineManager.stopProcess(machineId, processId);
    }

    @Path("/{machineId}/binding/{path:.*}")
    @PUT
    @RolesAllowed("user")
    public void bindProject(@PathParam("machineId") String machineId,
                            @PathParam("path") String path)
            throws NotFoundException, ServerException, ForbiddenException, ConflictException {
        checkCurrentUserPermissionsForMachine(machineManager.getMachine(machineId).getOwner());

        machineManager.bindProject(machineId, new ProjectBindingImpl().withPath(path));
    }

    @Path("/{machineId}/binding/{path:.*}")
    @DELETE
    @RolesAllowed("user")
    public void unbindProject(@PathParam("machineId") String machineId,
                              @PathParam("path") String path) throws NotFoundException, ServerException, ForbiddenException {
        checkCurrentUserPermissionsForMachine(machineManager.getMachine(machineId).getOwner());

        machineManager.unbindProject(machineId, new ProjectBindingImpl().withPath(path));
    }

    private void checkCurrentUserPermissionsForSnapshot(Snapshot snapshot) throws ForbiddenException, NotFoundException, ServerException {
        if (!EnvironmentContext.getCurrent().getUser().getId().equals(snapshot.getOwner())) {
            throw new ForbiddenException("You are not the owner of this snapshot");
        }
    }

    private void checkCurrentUserPermissionsForMachine(String machineOwner) throws ForbiddenException {
        final String userId = EnvironmentContext.getCurrent().getUser().getId();
        if (!userId.equals(machineOwner)) {
            throw new ForbiddenException("You are not the owner of this machine");
        }
    }

    private void checkCurrentUserPermissionsForWorkspace(String workspaceId) throws ForbiddenException, ServerException {
        try {
            final Member member = memberDao.getWorkspaceMember(workspaceId, EnvironmentContext.getCurrent().getUser().getId());
            if (member.getRoles().contains("workspace/admin") || member.getRoles().contains("workspace/developer")) {
                return;
            }
        } catch (NotFoundException ignored) {
        }
        throw new ForbiddenException("You are not a member of workspace " + workspaceId);
    }

    /**
     * Checks object reference is not {@code null}
     *
     * @param object
     *         object reference to check
     * @param subject
     *         used as subject of exception message "{subject} required"
     * @throws ForbiddenException
     *         when object reference is {@code null}
     */
    private void requiredNotNull(Object object, String subject) throws ForbiddenException {
        if (object == null) {
            throw new ForbiddenException(subject + " required");
        }
    }

    // make it package private for testing purposes
    LineConsumer getLineConsumer(String outputChannel) {
        final LineConsumer lineConsumer;
        if (outputChannel != null) {
            lineConsumer = new WebsocketLineConsumer(outputChannel);
        } else {
            lineConsumer = LineConsumer.DEV_NULL;
        }
        return lineConsumer;
    }

    private MachineDescriptor toDescriptor(String id,
                                           String machineType,
                                           MachineState machineState,
                                           String machineOwner,
                                           String workspaceId,
                                           Set<ProjectBinding> projects)
            throws ServerException {
        final List<ProjectBindingDescriptor> projectDescriptors = new ArrayList<>(projects.size());
        for (ProjectBinding project : projects) {
            projectDescriptors.add(dtoFactory.createDto(ProjectBindingDescriptor.class)
                                             .withPath(project.getPath())
                                             .withLinks(null)); // TODO
        }

        return dtoFactory.createDto(MachineDescriptor.class)
                         .withId(id)
                         .withType(machineType)
                         .withState(machineState)
                         .withOwner(machineOwner)
                         .withWorkspaceId(workspaceId)
                         .withProjects(projectDescriptors)
                         .withLinks(null); // TODO
    }

    private ProcessDescriptor toDescriptor(int processId, String commandLine, boolean isAlive) throws ServerException {
        return dtoFactory.createDto(ProcessDescriptor.class)
                         .withPid(processId)
                         .withCommandLine(commandLine)
                         .withIsAlive(isAlive)
                         .withLinks(null); // TODO
    }

    private SnapshotDescriptor toDescriptor(Snapshot snapshot) {
        final List<ProjectBindingDescriptor> projectDescriptors = new ArrayList<>(snapshot.getProjects().size());
        for (ProjectBinding projectBinding : snapshot.getProjects()) {
            projectDescriptors.add(dtoFactory.createDto(ProjectBindingDescriptor.class)
                                             .withPath(projectBinding.getPath())
                                             .withLinks(null));
        }

        return dtoFactory.createDto(SnapshotDescriptor.class)
                         .withId(snapshot.getId())
                         .withOwner(snapshot.getOwner())
                         .withImageType(snapshot.getImageType())
                         .withDescription(snapshot.getDescription())
                         .withCreationDate(snapshot.getCreationDate())
                         .withWorkspaceId(snapshot.getWorkspaceId())
                         .withProjects(projectDescriptors)
                         .withLinks(null);// TODO
    }
}
