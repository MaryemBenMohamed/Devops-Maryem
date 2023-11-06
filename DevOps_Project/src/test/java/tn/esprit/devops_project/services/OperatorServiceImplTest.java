package tn.esprit.devops_project.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class OperatorServiceImplTest {

    @Autowired
    private OperatorServiceImpl operatorServiceImpl;

    @Autowired
    private OperatorRepository operatorRepository;
    @Mock
    private OperatorRepository operatorRepoMock;
    @InjectMocks
    private OperatorServiceImpl operatorServiceMock;

    @Test
    void addOperator() {
        Operator operator = new Operator().builder().fname("Maryem").lname("BM").build();
        Operator operatorResult = operatorServiceImpl.addOperator(operator);

        assertThat(operator).isNotNull();
        assertThat(operator.getFname()).isNotEmpty();
        assertThat(operator.getLname()).isEqualTo("BM");

        Operator operatorFromDB = operatorRepository.findById(operatorResult.getIdOperateur()).orElse(null);
        assertThat(operatorFromDB).isNotNull();
        assertThat(operatorFromDB.getFname()).isEqualTo("Maryem");
        assertThat(operatorFromDB.getLname()).isEqualTo("BM");
    }

    @Test
    void addOperator2() {
        Operator operator = new Operator().builder().password("ABCDEFGHI").build();
        when(operatorRepoMock.save(operator)).thenReturn(operator);

        Operator operatorResult = operatorServiceMock.addOperator(operator);

        assertThat(operatorResult).isNotNull();
        assertThat(operatorResult.getPassword().length()).isGreaterThan(8);
        verify(operatorRepoMock, times(1)).save(operator);
    }


    @Test
    @Transactional
    void retrieveAllOperators() {
        operatorRepository.deleteAll();

        Operator op1 = new Operator();
        op1.setFname("MARYEEM");
        op1.setLname("BNM");
        op1.setPassword("nnn");
        operatorRepository.save(op1);

        Operator op2 = new Operator();
        op2.setFname("Malek");
        op2.setLname("CHI");
        op2.setPassword("SSSS");
        operatorRepository.save(op2);

        Operator op3 = new Operator();
        op3.setFname("Another");
        op3.setLname("User");
        op3.setPassword("password");
        operatorRepository.save(op3);

        List<Operator> operators = operatorServiceImpl.retrieveAllOperators();

        assertEquals(3, operators.size());

        Operator firstOperator = operators.get(0);
        assertEquals("MARYEEM", firstOperator.getFname());
        assertEquals("BNM", firstOperator.getLname());

        Operator secondOperator = operators.get(1);
        assertEquals("Malek", secondOperator.getFname());
        assertEquals("CHI", secondOperator.getLname());

        assertTrue(operators.contains(op3));
    }


    @Test
    @Transactional
    void retrieveAllOperators2() {
        List<Operator> operatorList = new ArrayList<>();
        Operator op1 = new Operator();
        op1.setFname("MARYEEM");
        op1.setLname("BNM");
        op1.setPassword("nnn");
        operatorList.add(op1);

        Operator op2 = new Operator();
        op2.setFname("Malek");
        op2.setLname("CHI");
        op2.setPassword("SSSS");
        operatorList.add(op2);

        Operator op3 = new Operator();
        op3.setFname("Another");
        op3.setLname("User");
        op3.setPassword("password");
        operatorList.add(op3);

        when(operatorRepoMock.findAll()).thenReturn(operatorList);

        List<Operator> operators = operatorServiceMock.retrieveAllOperators();

        assertEquals(3, operators.size());

        Operator firstOperator = operators.get(0);
        assertEquals("MARYEEM", firstOperator.getFname());
        assertEquals("BNM", firstOperator.getLname());

        Operator secondOperator = operators.get(1);
        assertEquals("Malek", secondOperator.getFname());
        assertEquals("CHI", secondOperator.getLname());

        assertTrue(operators.contains(op3));
    }

    @Test
    void deleteOperator() {
        Operator operator = new Operator();
        operator.setFname("SONIAA");
        operator.setLname("BA");
        operator.setPassword("BABA");
        operatorServiceImpl.addOperator(operator);

        Operator existingOperator = operatorRepository.findById(operator.getIdOperateur()).orElse(null);
        assertNotNull(existingOperator, "L'opérateur avec id" + operator.getIdOperateur() + "existe dans la bd");

        operatorServiceImpl.deleteOperator(operator.getIdOperateur());

        Operator operatorResult = operatorRepository.findById(operator.getIdOperateur()).orElse(null);
        assertNull(operatorResult, "L'opérateur avec cette id est supprimé de la bd");
    }
    @Test
    void deleteOperator2() {
        Operator operator = new Operator();
        operator.setIdOperateur(11L);
        operator.setFname("MARYEEM");
        operator.setLname("BNM");
        operator.setPassword("nnn");
        operatorServiceImpl.addOperator(operator);

        when(operatorRepoMock.findById(operator.getIdOperateur())).thenReturn(Optional.of(operator));

        operatorServiceMock.deleteOperator(operator.getIdOperateur());

        verify(operatorRepoMock).deleteById(operator.getIdOperateur());

        Operator operatorResult = operatorRepoMock.findById(operator.getIdOperateur()).orElse(null);
        assertNotNull(operatorResult, "L'opérateur avec id " + operator.getIdOperateur() + " est supprimé de la base de données");
    }



    @Test
    void addAndUpdateOperator() {
        Operator operator = new Operator();
        operator.setFname("MARYEMSAEE");
        operator.setLname("SAE");

        operatorServiceImpl.addOperator(operator);


        assertNotNull(operator.getIdOperateur(), "L'opérateur avec id doit exister dans la base de données après l'ajout");

        operator.setFname("Maleek");
        operator.setLname("CHI");

        operatorServiceImpl.updateOperator(operator);

        Operator updatedOperator = operatorRepository.findById(operator.getIdOperateur()).orElse(null);

        assertNotNull(updatedOperator, "L'opérateur avec id doit toujours exister dans la base de données après la mise à jour");

        assertEquals("Maleek", updatedOperator.getFname(), "Fname de l'opérateur est mis à jour");
        assertEquals("CHI", updatedOperator.getLname(), "Lname de famille de l'opérateur doit être mis à jour");
    }



    @Test
    void updateOperator2() {
        Operator operator = new Operator();
        operator.setIdOperateur(3L);
        operator.setFname("MARYEEM");
        operator.setLname("BNM");
        operatorServiceMock.addOperator(operator);
        when(operatorRepoMock.findById(operator.getIdOperateur())).thenReturn(Optional.of(operator));

        operator.setIdOperateur(3L);
        operator.setFname("Maleek");
        operator.setLname("CHI");
        operatorServiceMock.updateOperator(operator);

        //verify(operatorRepoMock).save(operator);

        Operator operatorResult = operatorRepoMock.findById(operator.getIdOperateur()).orElse(null);

        assertEquals("Maleek", operatorResult.getFname(), "Fname de l'opérateur est mis à jour");
        assertEquals("CHI", operatorResult.getLname(), "Lname de famille de l'opérateur doit être mis à jour");
    }

    @Test
    void retrieveOperator() {
        Operator operator = new Operator();
        operator.setFname("MARYEM");
        operator.setLname("BM");
        operator.setPassword("ABCDE");
        operatorServiceImpl.addOperator(operator);

        Long operatorId = operator.getIdOperateur();

        Operator operatorResult = operatorServiceImpl.retrieveOperator(operatorId);

        assertNotNull(operatorResult, "L'opérateur doit être trouvé dans la base de données");
        assertEquals("MARYEM", operatorResult.getFname(), "Fname de l'opérateur est incorrect");
        assertEquals("BM", operatorResult.getLname(), "Lname de l'opérateur est incorrect");
        assertEquals("ABCDE", operatorResult.getPassword(), "Password de l'opérateur est incorrect");
    }


    @Test
    void retrieveOperator2() {
        Operator operator = new Operator();
        operator.setIdOperateur(10L);
        operator.setPassword("ABCDEFGHIJKLMNSQY");
        operatorServiceMock.addOperator(operator);
        when(operatorRepoMock.findById(operator.getIdOperateur())).thenReturn(Optional.of(operator));

        Operator operatorResult = operatorServiceMock.retrieveOperator(operator.getIdOperateur());

        assertNotNull(operatorResult);

        assertEquals(operator,operatorResult);
        assertThat(operatorResult.getPassword().length()).isLessThan(20);
        verify(operatorRepoMock, times(1)).findById(operator.getIdOperateur());

    }



}