package za.co.bmw.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import za.co.bmw.kanban.model.Kanban;
import za.co.bmw.kanban.model.KanbanDTO;
import za.co.bmw.kanban.model.Task;
import za.co.bmw.kanban.model.TaskDTO;
import za.co.bmw.kanban.model.TaskStatus;
import za.co.bmw.kanban.repository.KanbanRepository;
import za.co.bmw.kanban.repository.TaskRepository;
import za.co.bmw.kanban.service.KanbanService;
import za.co.bmw.kanban.service.KanbanServiceImpl;
import za.co.bmw.kanban.service.TaskService;
import za.co.bmw.kanban.service.TaskServiceImpl;
import za.co.bmw.kanban.utility.KanbanUtil;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {
    TaskService taskService;
    @Mock
    TaskRepository taskRepository;

    @Before
    public void init() {
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    public void testSaveNewTask(){
        //given
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("develop kanban app");
        taskDTO.setColor("green");
        taskDTO.setStatus(TaskStatus.TODO);
        taskDTO.setDescription("this task is for developing backend for kanban app");


        //when
        when(taskRepository.save(KanbanUtil.convertDTOToTask(taskDTO))).thenReturn(KanbanUtil.convertDTOToTask(taskDTO));
        // then
        assertThat((taskService.saveNewTask(taskDTO)),is(equalTo(KanbanUtil.convertDTOToTask(taskDTO))));

    }
    @Test
    public void testGetTaskByTitle() {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("task1");
        taskDTO.setStatus(TaskStatus.INPROGRESS);
        taskDTO.setColor("Blue");
        taskDTO.setDescription("development item1");
        Task task = KanbanUtil.convertDTOToTask(taskDTO);

        Mockito.when(taskRepository.findByTitle("task1")).thenReturn(java.util.Optional.of(task));

        assertThat(taskService.getTaskByTitle("task1").get().getTitle(), is(equalTo(java.util.Optional.of(task).get().getTitle())));
    }

    @Test
    public void testGetAllTasks(){
        List<Task> tasks = new ArrayList<Task>();
        TaskDTO taskDTO1 = new TaskDTO();
        taskDTO1.setDescription("adding tasks");
        taskDTO1.setTitle("task1");
        taskDTO1.setStatus(TaskStatus.INPROGRESS);
        taskDTO1.setColor("green");
        Task task1= KanbanUtil.convertDTOToTask(taskDTO1);

        TaskDTO taskDTO2 = new TaskDTO();
        taskDTO2.setDescription("adding tasks");
        taskDTO2.setTitle("task2");
        taskDTO2.setStatus(TaskStatus.INPROGRESS);
        taskDTO2.setColor("Yellow");
        Task task2= KanbanUtil.convertDTOToTask(taskDTO2);
        tasks.add(task1);
        tasks.add(task2);

        Mockito.when(taskRepository.findAll()).thenReturn(tasks);

        assertThat(taskService.getAllTasks(),is(equalTo(tasks)));
    }

    @Test
    public void testDeleteTask(){
        TaskDTO taskDTO1 = new TaskDTO();
        taskDTO1.setDescription("adding tasks");
        taskDTO1.setTitle("task1");
        taskDTO1.setStatus(TaskStatus.INPROGRESS);
        taskDTO1.setColor("green");
        Task task1= KanbanUtil.convertDTOToTask(taskDTO1);

        Mockito.when(taskRepository.existsById(task1.getId())).thenReturn(false);
        assertFalse(taskRepository.existsById(task1.getId()));
    }

    @Test
    public void testGetTaskById(){
        //given
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("task title");
        taskDTO.setStatus(TaskStatus.TODO);
        taskDTO.setDescription("kanban dash development");
        taskDTO.setColor("green");
         Task task = KanbanUtil.convertDTOToTask(taskDTO);

        //when
        Mockito.when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));

        //then
        assertThat(taskService.getTaskById(1L),is(equalTo(java.util.Optional.of(task))));
    }
}
