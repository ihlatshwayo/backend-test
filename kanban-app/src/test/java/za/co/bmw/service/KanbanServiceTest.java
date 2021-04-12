package za.co.bmw.service;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.bmw.kanban.model.Kanban;
import za.co.bmw.kanban.model.KanbanDTO;
import za.co.bmw.kanban.model.Task;
import za.co.bmw.kanban.model.TaskDTO;
import za.co.bmw.kanban.model.TaskStatus;
import za.co.bmw.kanban.repository.KanbanRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import za.co.bmw.kanban.service.KanbanService;
import za.co.bmw.kanban.service.KanbanServiceImpl;
import za.co.bmw.kanban.utility.KanbanUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@RunWith(MockitoJUnitRunner.class)
public class KanbanServiceTest {

    KanbanService kanbanService;
    @Mock
    KanbanRepository kanbanRepository;


    @Before
    public void init() {
        kanbanService = new KanbanServiceImpl(kanbanRepository);
    }

    @Test
    public void testSaveNewkanban(){
        //given
        KanbanDTO kanbanDTO = new KanbanDTO();
        kanbanDTO.setTitle("kanban1");

        //when
        when(kanbanRepository.save(KanbanUtil.convertDTOToKanban(kanbanDTO))).thenReturn(KanbanUtil.convertDTOToKanban(kanbanDTO));
         // then
        assertThat((kanbanService.saveNewKanban(kanbanDTO)),is(equalTo(KanbanUtil.convertDTOToKanban(kanbanDTO))));

    }


    @Test
    public void when2KanbansInDatabase_thenGetListWithAllOfThem() {
        //given
        mockKanbanInDatabase(2);

        //when
        List<Kanban> kanbans = kanbanService.getAllKanbanBoards();

        //then
        assertEquals(2, kanbans.size());
    }

    @Test
    public void testGetKanbanById(){
        //given
        KanbanDTO kanbanDTO = new KanbanDTO();
        kanbanDTO.setTitle("kanban1");
        Kanban kanban1 = KanbanUtil.convertDTOToKanban(kanbanDTO);

        //when
        Mockito.when(kanbanRepository.findById(1L)).thenReturn(java.util.Optional.of(kanban1));

        //then
        assertThat(kanbanService.getKanbanById(1L),is(equalTo(java.util.Optional.of(kanban1))));
    }

    @Test
    public void testGetKanbanByTitle() {

        KanbanDTO kanbanDTO = new KanbanDTO();
        kanbanDTO.setTitle("kanban2");
        Kanban kanban2 = KanbanUtil.convertDTOToKanban(kanbanDTO);

        Mockito.when(kanbanRepository.findByTitle("kanban2")).thenReturn(java.util.Optional.of(kanban2));

        assertThat(kanbanService.getKanbanByTitle("kanban2").get().getTitle(), is(equalTo(java.util.Optional.of(kanban2).get().getTitle())));
    }

    @Test
    public void testUpdateKanban(){
        KanbanDTO kanbanDTO = new KanbanDTO();
        kanbanDTO.setTitle("kanban update");
        Kanban kanban = KanbanUtil.convertDTOToKanban(kanbanDTO);
        kanban.setId(1L);

        kanban.setTitle("change kanban");
        Mockito.when(kanbanRepository.save(kanban)).thenReturn(kanban);
        KanbanDTO newkanbanDTO = new KanbanDTO();
        newkanbanDTO.setTitle("kanban update");

        assertThat(kanbanService.updateKanban(kanban,newkanbanDTO),is(equalTo(kanban)));

    }
    @Test
    public void testDeleteKanban(){
        KanbanDTO kanbanDTO = new KanbanDTO();
        kanbanDTO.setTitle("adding kanban");

        Kanban kanban= KanbanUtil.convertDTOToKanban(kanbanDTO);

        Mockito.when(kanbanService.deleteKanban(kanban))
        Mockito.when(kanbanRepository.existsById(kanban.getId())).thenReturn(false);

        assertFalse(kanbanRepository.existsById(kanban.getId()));
    }


    private void mockKanbanInDatabase(int kanbanCount) {
        when(kanbanRepository.findAll())
                .thenReturn(createKanbanList(kanbanCount));
    }

    private List<Kanban> createKanbanList(int kanbanCount) {
        List<Kanban> kanbans = new ArrayList<>();
        IntStream.range(0, kanbanCount)
                .forEach(number ->{
                    Kanban kanban = new Kanban();
                    kanban.setId(Long.valueOf(number));
                    kanban.setTitle("Kanban " + number);
                    kanban.setTasks(new ArrayList<>());
                    kanbans.add(kanban);
                });
        return kanbans;
    }
}
