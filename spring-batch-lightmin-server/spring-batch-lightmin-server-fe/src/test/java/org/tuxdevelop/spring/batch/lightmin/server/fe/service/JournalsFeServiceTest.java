package org.tuxdevelop.spring.batch.lightmin.server.fe.service;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tuxdevelop.spring.batch.lightmin.server.domain.Journal;
import org.tuxdevelop.spring.batch.lightmin.server.fe.model.journal.JournalModel;
import org.tuxdevelop.spring.batch.lightmin.server.service.JournalServiceBean;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JournalsFeServiceTest {

    @Mock
    private JournalServiceBean journalServiceBean;
    @InjectMocks
    private JournalsFeService journalsFeService;

    @Test
    public void testGetAll() {
        final int count = 10;
        final List<Journal> journals = ServiceTestHelper.createJournals(count);

        when(this.journalServiceBean.findAll()).thenReturn(journals);

        final List<JournalModel> result = this.journalsFeService.getAll();

        BDDAssertions.then(result).isNotNull();
        BDDAssertions.then(result.size()).isEqualTo(count);
    }
}
