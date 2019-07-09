package com.vaadin.tests.components.upload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;

import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.UploadElement;
import com.vaadin.tests.tb3.SingleBrowserTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DisablingUploadTest extends SingleBrowserTest {

    ButtonElement button;
    ButtonElement pushButton;
    ButtonElement stateButton;

    @Override
    public void setup() throws Exception {
        super.setup();

        openTestURL();
    }

    @Test
    public void buttonWorksAsExpected() {
        buttonGroup();

        // Disable button is working
        assertTrue("Upload button should be enabled",
                getSubmitButton().isEnabled());
        button.click();
        assertFalse("Upload button should be disabled",
                getSubmitButton().isEnabled());

        // pushmode button is working
        assertEquals("Set the Push Mode", pushButton.getCaption());
        pushButton.click();
        sleep(100);
        assertEquals("enable push mode", pushButton.getCaption());
        pushButton.click();
        sleep(100);
        assertEquals("disable push mode", pushButton.getCaption());

        // upload button state is correct
        assertEquals("true", stateButton.getCaption());
        stateButton.click();
        sleep(100);
        assertEquals("false", stateButton.getCaption());
    }

    @Test
    public void pushEnabled_uploadFile_uploadButtonDisabled() throws Exception {
        buttonGroup();

        uploadFile(false);

        String expected = "2. File has been uploaded.";

        String actual = getLogRow(0);
        assertEquals("Upload log row does not match expected", expected,
                actual);

        stateButton.click();
        sleep(100);
        assertEquals("false", stateButton.getCaption());

        uploadFile(false);
        //assert no new log
        assertEquals("Upload log row does not match expected", expected,
                actual);
    }

    @Test
    public void pushDisabled_uploadFile_uploadButtonDisabled() throws Exception {
        buttonGroup();

        pushButton.click();

        uploadFile(false);

        String expected = "2. File has been uploaded.";

        String actual = getLogRow(0);
        assertEquals("Upload log row does not match expected", expected,
                actual);

        stateButton.click();
        sleep(100);
        assertEquals("false", stateButton.getCaption());

        uploadFile(false);
        //assert no new log
        assertEquals("Upload log row does not match expected", expected,
                actual);
    }

    @Test
    public void pushEnabled_uploadLargeFile_uploadButtonDisabled() throws Exception {
        buttonGroup();

        uploadFile(true);

        String expected = "2. File has been uploaded.";

        String actual = getLogRow(0);
        assertEquals("Upload log row does not match expected", expected,
                actual);

        stateButton.click();
        sleep(100);
        assertEquals("false", stateButton.getCaption());

        uploadFile(true);
        //assert no new log
        assertEquals("Upload log row does not match expected", expected,
                actual);
    }

    @Test
    public void pushDisabled_uploadLargeFile_uploadButtonDisabled() throws Exception {
        buttonGroup();

        pushButton.click();

        uploadFile(true);

        String expected = "2. File has been uploaded.";

        String actual = getLogRow(0);
        assertEquals("Upload log row does not match expected", expected,
                actual);

        stateButton.click();
        sleep(100);
        assertEquals("false", stateButton.getCaption());

        uploadFile(true);
        //assert no new log
        assertEquals("Upload log row does not match expected", expected,
                actual);
    }

    private void buttonGroup() {
        button = $(ButtonElement.class).id("button-id");
        pushButton = $(ButtonElement.class).id("push-button");
        stateButton = $(ButtonElement.class).id("state-button");
    }

    private void uploadFile(boolean large) throws Exception {
        File tempFile = createTempFile(large);
        fillPathToUploadInput(tempFile.getPath());

        getSubmitButton().click();
        sleep(100);
    }

    /**
     * @return The generated temp file handle
     * @throws IOException
     */
    private File createTempFile(boolean large) throws IOException {
        File tempFile = File.createTempFile("TestFileUpload", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        if(large) {
            writer.write(getLargeTempFileContents());
        } else {
            writer.write(getTempFileContents());
        }
        writer.close();
        tempFile.deleteOnExit();
        return tempFile;
    }

    private String getTempFileContents() {
        return "This is a test file!\nRow 2\nRow3";
    }

    private String getLargeTempFileContents() {
        String LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi lacinia sollicitudin neque, vitae cursus eros scelerisque sit amet. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Proin sollicitudin tempus lorem, sed consectetur nibh ultrices nec. Ut tempus laoreet dolor non molestie. Quisque consectetur tellus ut tortor imperdiet semper. In tempor odio eu metus hendrerit pharetra. Aliquam erat volutpat. Aliquam erat volutpat. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec sit amet turpis facilisis ipsum cursus viverra ut vitae turpis. Integer tincidunt sem id sem tristique a laoreet eros euismod. Ut quis leo vel neque pellentesque ullamcorper. Aliquam at fermentum justo. Sed eget laoreet elit. Pellentesque commodo dui quis metus ornare bibendum. Ut tempus, ipsum in euismod scelerisque, augue ante lacinia sem, scelerisque imperdiet felis lectus rhoncus felis.Fusce vitae nisl lorem, id ultricies massa. Phasellus augue eros, dapibus vel fermentum non, sodales id mi. Nulla tincidunt diam a justo ultricies vestibulum nec sed tellus. Morbi faucibus leo et odio condimentum at porttitor diam auctor. Aenean volutpat lacinia mauris. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean malesuada ullamcorper elit ut eleifend. Etiam tempor, mauris id aliquet tempor, erat felis rutrum eros, a aliquam dolor nunc nec enim. Sed enim dolor, tristique ut tempus vitae, elementum at velit. Duis vulputate ultricies risus, ac gravida erat tincidunt posuere. Pellentesque dapibus tincidunt rhoncus. Phasellus eleifend molestie eros. Praesent id imperdiet urna. Etiam fermentum interdum quam, in tempus quam condimentum at.Curabitur eget ultrices augue. Suspendisse potenti. Nam hendrerit, dolor eget adipiscing lobortis, enim risus dignissim orci, a imperdiet tellus nulla id nibh. Praesent eu dui sit amet nulla interdum dignissim vel quis ante. Sed a lectus metus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Vivamus eget ultrices neque. Cras adipiscing condimentum justo adipiscing consequat. In accumsan mollis sapien ut euismod. Nulla sodales pulvinar leo, nec condimentum leo vestibulum ut. Phasellus risus metus, aliquet quis porta at, imperdiet vitae metus. Proin nec odio odio. Quisque nec elit id lacus iaculis ullamcorper sit amet vel ipsum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. In non lacus sed libero euismod rhoncus. Suspendisse augue massa, ullamcorper at accumsan at, porttitor ac odio.Morbi imperdiet, lectus vehicula eleifend ornare, lectus arcu ullamcorper augue, a imperdiet eros augue id justo. Integer eget pretium lorem. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Cras vehicula leo eget metus consectetur viverra. Nullam a mi nisl, in lacinia metus. Curabitur mollis eleifend augue, consectetur imperdiet velit sagittis aliquet. In tempor tempor orci eu faucibus. Mauris nisi neque, vulputate eu dignissim eget, aliquet sed nibh. Mauris rhoncus elit eget lectus dignissim bibendum. Phasellus sed nunc lacus, nec aliquet elit. Fusce blandit, sem vitae gravida elementum, sem orci porttitor eros, id porta tortor massa ut velit. Nulla pretium mi at tortor porttitor sollicitudin.Ut gravida est at lectus eleifend non ultrices ligula blandit. Sed purus lectus, adipiscing ut mollis vel, interdum quis diam. Phasellus eget nibh augue. Fusce justo felis, facilisis in auctor ut, cursus et enim. Praesent hendrerit venenatis elit, iaculis cursus ligula auctor in. Proin mollis malesuada dolor at elementum. Maecenas sit amet leo odio. Integer ac enim in justo eleifend dapibus. Pellentesque consequat, libero congue sagittis pulvinar, odio eros porta quam, et aliquet dolor turpis non lacus. Sed eu risus nunc. Aliquam tempus nulla a purus auctor mattis. Nullam adipiscing nisl nec purus porttitor rutrum. Sed pellentesque tincidunt posuere. Nulla varius cursus eros. Phasellus vel neque at metus tempus placerat laoreet ac dolor. Nulla faucibus leo nisi, vel placerat lectus.Donec non leo lacinia metus euismod dignissim suscipit ut neque. Donec iaculis, lacus ut luctus bibendum, mi libero tristique sem, vel consectetur risus ligula in turpis. Nulla lorem justo, porttitor non sollicitudin at, pharetra non ante. Phasellus tristique, purus at luctus auctor, nisl neque congue lacus, eget convallis mauris libero sit amet arcu. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tellus quam, lobortis vitae commodo id, ultrices a augue. Nam tellus erat, varius vel iaculis et, fermentum ut tellus. Donec id urna orci. Curabitur eleifend hendrerit libero, id tempor dolor faucibus eu. Quisque aliquam sapien a tortor mollis sollicitudin. Duis augue erat, sagittis eget consequat vel, volutpat et augue. Sed vehicula mattis nisi, non porta elit imperdiet vitae. Curabitur scelerisque mollis lectus, consequat suscipit est ultricies sit amet. Aenean in sem libero, id adipiscing urna. Aenean eleifend posuere lorem in aliquet. Praesent arcu est, tristique quis adipiscing ac, tincidunt nec dui. Nunc sed leo quis justo posuere suscipit. Fusce vitae lectus vitae urna sodales aliquam.Sed velit purus, ullamcorper non interdum quis, facilisis eget tortor. Etiam vestibulum venenatis aliquet. Fusce felis felis, venenatis id tincidunt at, vehicula et erat. Curabitur eu sagittis nisl. Curabitur nisi sapien, venenatis nec pulvinar ut, tempor id diam. Suspendisse metus libero, placerat in fermentum a, aliquam id arcu. Nulla quis accumsan massa. Curabitur consectetur pulvinar sapien non consequat. Etiam sem dolor, posuere id laoreet a, tempor vel nisl. Fusce eu lacus orci. Pellentesque tellus dui, pulvinar nec egestas vitae, adipiscing euismod nunc. Mauris consequat felis sit amet justo pellentesque venenatis. Donec dignissim porta dolor, eget pellentesque lorem porta eu. Donec eget enim eu leo mattis ornare in ut libero. Maecenas sit amet ante mauris.Praesent ultrices diam id lectus viverra sed lobortis risus adipiscing. Nulla in risus est. Proin vitae dolor ligula, a placerat enim. Integer hendrerit erat tincidunt leo malesuada sit amet tincidunt justo mattis. Morbi ut neque urna, ut blandit erat. Donec eleifend justo ac augue ultrices consectetur. In rutrum, leo ac porttitor volutpat, neque mi lacinia diam, id fermentum risus neque vitae risus. Suspendisse in justo libero, eget ultrices nisl. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. In sollicitudin, arcu et convallis fermentum, est tellus pellentesque neque, ac faucibus tellus orci non purus. Ut aliquet facilisis ullamcorper. Cras justo ipsum, mollis vel ultricies non, consectetur a libero.Aliquam erat volutpat. Nunc ut mauris eu lorem consequat placerat. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Suspendisse sapien nisi, pellentesque ut porttitor eget, auctor vitae massa. Fusce et euismod massa. In dolor enim, commodo vitae scelerisque sit amet, vulputate quis nibh. Donec ut mollis elit. Nullam placerat sem at urna euismod tempus vitae a mi. Curabitur ac sem non tortor dapibus euismod in et augue. Phasellus elit nisi, ornare eget aliquam et, convallis vitae eros. Vivamus vitae facilisis quam. Nullam a dolor sit amet ante sodales vulputate. Sed vel elit libero. Vestibulum vestibulum porttitor orci, a elementum risus congue in. Mauris ut justo sed dolor rhoncus viverra nec non eros. Phasellus dignissim, mi id laoreet venenatis, enim lacus pharetra arcu, sit amet consectetur velit elit hendrerit arcu. Integer hendrerit suscipit pretium. Proin orci quam, vehicula vel accumsan tempor, facilisis tempus tortor.Etiam et nunc id odio lobortis ornare ac eget lorem. Proin tristique iaculis felis, ut gravida lacus congue ut. Etiam nibh turpis, congue vel ornare ut, feugiat ut mi. Etiam id ante eget turpis laoreet dapibus. Vivamus accumsan sem at est lacinia sagittis vitae a erat. Etiam mollis justo et felis faucibus sodales. Etiam placerat felis sed ante sollicitudin dignissim. Nunc fringilla nunc in lacus ultricies rutrum. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed venenatis porta turpis dignissim tempor. Etiam consequat vulputate elementum. Sed augue lacus, aliquam eu ornare vel, aliquam at turpis.Ut lacinia velit odio. Nulla sapien tellus, fringilla dignissim porta sed, elementum eget diam. Quisque pulvinar commodo massa, vitae lobortis velit iaculis id. Vestibulum id dolor vel mauris auctor commodo id at odio. Suspendisse consectetur consequat erat. Aliquam in purus at sem pretium ultricies. In urna elit, porttitor vel gravida at, varius non nisi. Nullam id eros at lorem posuere ullamcorper eget in dui. Sed sit amet mi vel quam vestibulum imperdiet. Ut ullamcorper purus nibh, a commodo tortor. Phasellus pretium, nulla ut venenatis euismod, augue arcu volutpat nulla, id semper est purus eget urna. Vivamus pharetra diam ac ante cursus tempus.Cras in rhoncus felis. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In semper leo sit amet nisi rutrum et interdum odio fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Cras sollicitudin dui non risus iaculis a lobortis odio consequat. Sed consectetur nisl eu nisi pulvinar fermentum. Etiam placerat tincidunt pellentesque. Etiam luctus auctor nulla ac pulvinar. Pellentesque sit amet eros est. Sed hendrerit sem rhoncus purus rhoncus ullamcorper. Integer vitae commodo sem. Donec orci lectus, gravida sit amet viverra ut, suscipit eu odio. Vivamus ac dolor sit amet augue porttitor imperdiet nec quis mauris. Etiam luctus pharetra lectus sed mattis. Proin tincidunt ultricies dolor, at sagittis ante tincidunt ac.Morbi sit amet sem ut leo ornare luctus at sed odio. Donec vestibulum consequat consectetur. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Integer sit amet leo eget ipsum eleifend commodo et vel sapien. Donec vulputate facilisis nisi in bibendum. Vestibulum justo magna, mollis in varius a, malesuada id nisl. Vestibulum fermentum leo at ipsum euismod scelerisque. Donec id diam eget sapien varius ultricies. Quisque mollis vehicula lorem, vitae suscipit lorem gravida vel. Cras ipsum odio, pellentesque vitae molestie at, facilisis eget felis. Morbi gravida velit eget ante posuere id pharetra dolor vehicula. Praesent lectus sem, pretium sit amet eleifend non, ultricies vitae eros.Etiam consequat felis quis elit semper sed lobortis risus faucibus. Quisque diam ante, gravida ut mollis tristique, auctor ac mauris. Phasellus vestibulum sapien et justo sagittis sed dignissim dui sollicitudin. Maecenas egestas quam et odio mattis eu posuere est sagittis. Nulla nec posuere turpis. In mauris sapien, pharetra et placerat eu, dictum quis justo. Curabitur sit amet varius nulla. Cras feugiat erat quis ante aliquam blandit. In ac libero id massa dapibus ultrices. Donec non massa orci.In gravida justo vitae eros ultrices fringilla. Sed mi quam, lobortis eu pharetra nec, pulvinar eu justo. Sed ornare diam sed nulla pellentesque at tincidunt lacus cursus. Donec vel neque et urna euismod suscipit. Proin in tortor eget felis euismod ultrices. Nunc ut orci elit, vitae pellentesque ligula. Vivamus in mi felis, at congue nulla. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi mattis, ligula id sollicitudin posuere, eros odio pellentesque felis, in auctor libero mi nec massa. Praesent fringilla pretium tempus. Sed quis leo nulla, a pretium mi. Sed malesuada quam eget lorem tristique eget accumsan libero auctor. Vestibulum metus nisl, rutrum vel ullamcorper scelerisque, ultrices cursus purus. Aliquam convallis sollicitudin elit at mattis. Phasellus vitae magna id quam laoreet gravida quis ac metus.Duis lobortis arcu a elit porta laoreet. Pellentesque at condimentum sapien. Maecenas rutrum eleifend quam, eu pellentesque quam tincidunt ac. Vivamus fermentum justo ac dui dapibus ut tempus massa congue. Phasellus fermentum placerat enim, et dignissim mi feugiat in. Etiam odio diam, mollis vitae viverra a, egestas eget arcu. Curabitur purus lectus, accumsan quis lobortis ut, faucibus ac magna. Vivamus consectetur, ligula sit amet convallis rhoncus, risus lorem scelerisque arcu, eu fermentum felis leo sit amet eros. In hac habitasse platea dictumst. Sed congue pretium erat, vel hendrerit velit dapibus ut. Aliquam porta porttitor nunc facilisis pretium. Aliquam tincidunt porttitor tincidunt. Sed at erat a arcu elementum dignissim nec et ipsum. In et magna erat. In hac habitasse platea dictumst. Nulla sit amet enim vitae magna ullamcorper auctor ac id lectus. Fusce id sapien massa.Fusce porta nisl non leo iaculis sed elementum purus viverra. Aenean vitae erat elit. Proin id tincidunt nibh. Fusce faucibus dignissim laoreet. Etiam volutpat ultricies magna in euismod. Quisque luctus malesuada massa, ac ullamcorper dui luctus quis. Mauris a felis ut urna blandit semper egestas vel dui. Nulla facilisi. Ut dapibus bibendum bibendum. Curabitur id facilisis tellus. Vestibulum arcu orci, pretium vestibulum accumsan ut, feugiat eget eros. Mauris dapibus sem a massa ornare id posuere purus ornare. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Phasellus mollis, orci ac eleifend pharetra, risus turpis euismod urna, ut congue neque nunc ut felis. Aliquam a nisi leo. Nulla consequat faucibus ligula consectetur tristique. Duis at lacus orci, eget pulvinar tortor.Aenean nisi justo, dictum ac sagittis a, mollis ut orci. Donec faucibus congue neque ut gravida. Nam interdum nunc vel ipsum molestie elementum. Aliquam a eros non mi accumsan lacinia ut eu justo. Donec id ipsum purus, sed congue nunc. Donec euismod ante quis metus interdum feugiat. Sed nec dictum neque. Vivamus sed felis non lorem placerat facilisis eu sit amet nisi. Morbi dolor dolor, malesuada eu congue at, sollicitudin ut metus. In sit amet ipsum purus, ut dignissim dolor. Pellentesque quis velit velit. Aenean dapibus ipsum eu felis placerat volutpat. Donec auctor dictum ligula ut facilisis. Aliquam vulputate nunc nec diam ultricies auctor dictum dui fringilla. Cras urna metus, porttitor et sodales eu, tempus non mauris. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.Etiam tempor, mi ut tempor adipiscing, nunc lorem gravida odio, scelerisque aliquet ante massa id nibh. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus eu nisl urna. Quisque vel leo in lorem bibendum bibendum ut eget massa. Morbi dictum, metus eu pellentesque lobortis, augue nisi facilisis velit, sit amet feugiat risus nunc vitae elit. Fusce blandit mi a augue gravida eu tristique sem consequat. Suspendisse non malesuada nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Quisque commodo lorem id ipsum cursus posuere. Proin euismod facilisis est, vel imperdiet lectus commodo blandit. Proin luctus dapibus purus, id vulputate mauris tincidunt vel. Vestibulum volutpat molestie nulla faucibus malesuada. Aenean ut ornare lectus.Maecenas vel mauris in dolor imperdiet gravida eu nec mi. Vestibulum varius sem et nibh aliquet eu posuere tellus luctus. Fusce blandit, odio ut gravida semper, tellus lorem porta felis, vitae posuere mi lorem eget magna. Sed tristique nisi ut ipsum adipiscing molestie. Donec volutpat nunc eget neque posuere aliquam. Vivamus vel sem lectus. In volutpat dictum eros. Curabitur mollis pretium arcu ut rhoncus. Vivamus eros risus, sagittis posuere suscipit sit amet, consectetur eget elit. Mauris porttitor feugiat magna, sed lobortis ante porttitor at. Etiam non enim quam, nec tempus erat. Ut porta pharetra nibh vel laoreet. Sed euismod elit eget sem tempus non sodales massa cursus. Aliquam erat volutpat. Nulla a tortor ornare nibh feugiat venenatis. Mauris interdum mattis odio eget porta. Maecenas ac ullamcorper velit. Nulla id sagittis ligula. Aliquam pharetra, diam ut euismod pellentesque, sapien ligula sagittis nisi, sed dictum dolor purus sit amet elit.Aenean quis metus velit. In at fermentum nunc. Cras in magna sapien, sed vestibulum lacus. Sed pellentesque, massa sed malesuada tincidunt, erat tortor consectetur lacus, vel congue arcu enim id libero. Donec elit neque, egestas a faucibus ut, tempor nec diam. Donec turpis est, vulputate id venenatis vitae, vulputate sit amet elit. Ut semper nibh orci. Fusce nibh nisi, tristique eu dapibus id, faucibus nec augue. Sed sagittis malesuada leo, pretium porttitor ante lacinia in. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.Sed quis libero ante, ac consequat nisl. Cras nisl urna, ultrices eu dapibus sed, pretium quis diam. Curabitur aliquam, dolor et convallis viverra, purus orci commodo neque, a sagittis felis tellus quis mi. In mi orci, tempus ut sodales imperdiet, condimentum ac tortor. Phasellus ornare, tortor eget egestas ultricies, est risus ultricies lorem, ac tempus nibh ligula ac ante. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec fermentum nibh vitae tortor laoreet sed rutrum arcu suscipit. Fusce iaculis nisl in justo pretium a interdum ipsum dignissim. Sed sed semper metus. Aenean ante turpis, fermentum vestibulum dapibus eget, dictum id leo. Nam nec lorem erat.Cras dolor nulla, suscipit nec viverra ac, egestas vitae enim. Quisque vel laoreet sem. Sed non felis at nisl ultricies pharetra vitae a lorem. Suspendisse tempor arcu id massa laoreet congue. Nam eu turpis justo, nec adipiscing tortor. Etiam mattis lorem tincidunt diam molestie semper. Vivamus rutrum est in tortor egestas consequat. Sed vitae ultrices leo. Aenean pulvinar malesuada enim, eget elementum purus bibendum a. Vivamus sit amet mauris mauris, placerat varius lorem. Sed diam tortor, posuere non convallis molestie, dictum quis libero. Nulla quis tellus a nunc semper elementum vel sed odio. Cras ac mattis lectus. Proin placerat imperdiet sodales. Fusce quis magna dolor, eget porttitor lectus. Mauris elementum lorem vitae lectus porta scelerisque nec a sapien. Vivamus dapibus volutpat leo nec vehicula. Praesent ac diam ipsum.Pellentesque nec ullamcorper nulla. Nam lectus dolor, porttitor vitae varius sed, egestas a arcu. Suspendisse malesuada venenatis erat nec consequat. Morbi consequat magna a neque ultrices eget aliquet sapien ultrices. Aenean vel diam ipsum, ac scelerisque dui. Morbi quis arcu neque, eget auctor enim. Etiam pretium est sit amet nunc porta ornare vel quis sapien. Aliquam pulvinar pharetra risus, vel mollis mauris cursus sed. In sagittis nisl ac nunc placerat bibendum. Proin in risus lorem, sed fringilla eros.Proin luctus aliquam metus vitae euismod. Maecenas hendrerit sollicitudin hendrerit. Etiam magna elit, fringilla ut suscipit at, viverra eget ante. Morbi consequat, dui non tristique varius, eros ipsum vulputate sem, eu tristique nibh lacus vel felis. Maecenas eget eros a urna consectetur interdum. Integer est nibh, semper sed feugiat quis, lacinia eget diam. In ultricies mollis lectus sed imperdiet. Proin sed magna nec tortor blandit lacinia in sit amet nibh. Nullam ultrices tempor lacus, vel tincidunt eros vulputate euismod. Praesent justo nibh, convallis ut vehicula consequat, molestie posuere nulla. Mauris sollicitudin egestas vestibulum. Suspendisse auctor imperdiet eros, sit amet dignissim magna dapibus in. Praesent ultrices sem et risus interdum vel pharetra dolor tristique. Proin eleifend nunc eget risus venenatis posuere. Sed est nisi, interdum porta dapibus a, laoreet ac mi. Praesent sit amet varius lectus. Phasellus suscipit lacinia velit, sed tempus odio varius at. Fusce eleifend elementum aliquet. Proin ultricies libero at ipsum condimentum rhoncus.Quisque convallis, arcu at vehicula imperdiet, velit eros posuere sem, sit amet pulvinar purus massa a tellus. Maecenas cursus neque in dolor facilisis ac aliquam ligula eleifend. Fusce quis faucibus nisl. Fusce pulvinar magna vel enim scelerisque id cursus tortor faucibus. Nunc est turpis, iaculis ac sodales non, euismod vitae nunc. Fusce felis purus, iaculis dapibus mollis quis, condimentum consequat eros. Ut a lectus nec mi lacinia fringilla. Donec eget metus lectus, et rhoncus enim. Donec pharetra malesuada enim at rutrum. Vestibulum tincidunt gravida viverra. Vestibulum commodo sem nisl.Vivamus iaculis mollis arcu, quis dapibus risus semper quis. Quisque consectetur ullamcorper arcu id lacinia. Proin non tellus tellus. Nunc vehicula, arcu in pharetra feugiat, ante ante viverra urna, at convallis ligula odio id purus. Morbi quis orci non ante imperdiet luctus. Maecenas et ipsum est, at viverra sem. Morbi placerat ultrices imperdiet. Nullam id arcu at quam mollis hendrerit eu ac lectus. Etiam a neque sed turpis vestibulum posuere. Ut elementum tincidunt nulla at pellentesque. Integer nisl nunc, faucibus non sollicitudin eu, vestibulum ut nulla. In auctor est blandit purus rhoncus et sagittis eros convallis. Mauris sodales ultricies varius. Nulla facilisi. Donec ut nisl nunc, id accumsan erat. Praesent gravida molestie lobortis. In suscipit nisi sed elit sagittis tristique. Pellentesque vitae ante elit. Nullam diam est, ultrices vitae molestie at, tempus et magna. In hac habitasse platea dictumst.Vestibulum id tristique quam. Vivamus dui nulla, pretium eget elementum et, pulvinar a diam. Sed sed arcu sapien, et pharetra nisl. Nunc dignissim, lectus ac fringilla cursus, risus ante pretium justo, nec molestie sem urna id velit. Aenean tincidunt felis sed risus vestibulum ultricies. Aliquam lacinia scelerisque hendrerit. Morbi tincidunt molestie sapien sed faucibus. Vivamus et nisl velit, in semper libero. Duis elementum, dolor eget euismod porttitor, tortor eros tristique risus, eget posuere purus leo venenatis leo. Quisque in elit hendrerit felis accumsan gravida id sed sem.Sed massa turpis, interdum eget adipiscing a, tempor non enim. Vivamus sit amet purus tortor, quis tincidunt nunc. Nam vehicula dolor vitae est bibendum sed condimentum nisl pretium. Aenean pellentesque egestas lacinia. Vestibulum id felis sapien, vestibulum pharetra mauris. Donec quis ligula quis risus ultrices pharetra ac et quam. Nunc aliquam adipiscing mauris id euismod. Suspendisse potenti. Ut pharetra tristique pellentesque. Fusce tristique nisl vitae eros adipiscing lobortis. Ut porttitor porttitor est, vel euismod mi feugiat quis. Sed bibendum lacus vel sem hendrerit a ultricies sapien mattis.Vestibulum a tempus urna. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aenean tincidunt interdum dui, in accumsan ante placerat non. Aliquam suscipit eros eget quam ullamcorper consequat. Nulla facilisi. Phasellus porttitor interdum quam nec mattis. Etiam vitae diam vel est tempus aliquam eu et tellus. Phasellus lobortis porttitor mi, eu mollis augue tempus eu. Morbi a urna id nibh faucibus dictum. Sed hendrerit fringilla nunc vel elementum. Donec vel massa vitae nibh imperdiet vestibulum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Vestibulum in velit lorem, eu posuere felis. Maecenas ornare posuere faucibus. Donec dignissim ante eu neque suscipit interdum. Pellentesque ultrices dictum quam, tempor tempus leo posuere eget. Vestibulum dictum interdum urna, at feugiat neque venenatis ac. Donec tempus dictum enim, nec tempor neque rhoncus vel.Sed mollis luctus leo sed mattis. Vestibulum tempor mattis vehicula. Duis id elementum libero. Integer condimentum tincidunt molestie. Aenean quam tortor, rutrum sit amet placerat sed, sollicitudin eget turpis. In hac habitasse platea dictumst. Mauris lacinia mollis rhoncus. Fusce nec tellus elementum magna congue ultricies. Nulla consequat, dui nec facilisis hendrerit, ligula sapien viverra velit, at tempor risus leo vestibulum risus. Cras volutpat molestie orci ullamcorper gravida. Nullam est nisi, tempus et tincidunt ut, dapibus vitae nibh. Nam scelerisque purus sit amet odio imperdiet mattis. Integer ut ultrices odio.Cras placerat turpis nisl. Quisque consectetur urna vel nunc faucibus dictum. Nunc ac varius mauris. In ut egestas purus. Sed arcu nibh, mollis vitae dapibus eget, consectetur sit amet eros. Curabitur tincidunt purus sagittis neque adipiscing pretium. Aliquam tempor ullamcorper lectus. Cras quis elit et sapien euismod dapibus vitae in urna. Aliquam adipiscing urna non elit suscipit hendrerit. Mauris euismod tincidunt tortor eu vestibulum. Praesent ac quam massa. Fusce vitae viverra justo. Phasellus vel posuere augue. Praesent sagittis auctor odio in cursus. Sed vel lacus velit, vitae eleifend leo. Donec dapibus ornare mauris ut imperdiet. Nullam a sem felis, in commodo ligula. Nunc sed justo id metus bibendum pretium. Fusce lacinia lorem at leo ultricies porta.Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Morbi non ipsum nisi, at porta ipsum. Nulla dolor lacus, auctor eget tincidunt et, tempor in velit. Cras vel diam facilisis dolor sagittis sodales. In hac habitasse platea dictumst. Donec dignissim vulputate tempor. Fusce tristique vestibulum vehicula. Integer nec erat a risus semper ultrices nec ac eros. Phasellus egestas, risus eget suscipit iaculis, mi magna posuere urna, in sollicitudin odio leo nec lorem. Ut at enim nunc, eget pellentesque ipsum. Nam at ipsum et magna tempor gravida vestibulum ut diam. Aenean aliquam scelerisque blandit. Cras sapien sem, consectetur a laoreet at, mattis vel lorem. Maecenas enim mauris, hendrerit ac imperdiet in, hendrerit a justo. Vestibulum justo metus, adipiscing at pulvinar at, scelerisque at velit. Aliquam sodales feugiat tortor ullamcorper cursus. Quisque tortor felis, sodales eu bibendum in, pellentesque eget urna.Nullam ultricies congue nisi nec blandit. Nulla nisl mi, aliquet vel dictum in, pellentesque fringilla arcu. Nam vulputate iaculis pellentesque. Morbi faucibus, augue vitae aliquam fringilla, ipsum tellus lacinia ligula, a luctus mauris orci id nulla. Vestibulum commodo, diam eget tincidunt iaculis, leo ante suscipit ligula, convallis scelerisque quam augue quis libero. Pellentesque vulputate ultricies eros a porta. In hac habitasse platea dictumst. Quisque at sem vel nisl pellentesque pellentesque at imperdiet turpis. Sed quis sem non nulla pellentesque placerat. Etiam sed ultricies nisl. Nunc vel leo augue, vitae fringilla urna. Suspendisse eget dolor enim, sed interdum lorem. Etiam imperdiet, nunc a tempor elementum, ipsum ante sodales tortor, id dignissim arcu diam vitae metus.Sed tincidunt fringilla mauris a lobortis. Morbi mollis elementum purus ac auctor. Etiam malesuada sapien vehicula magna fringilla sit amet porta sem scelerisque. Vestibulum malesuada ante nunc. Curabitur laoreet commodo mauris eu tempor. Sed hendrerit, lorem vel scelerisque aliquam, metus ligula ultricies ante, at luctus neque risus ut nisi. Suspendisse lectus sapien, tempor eget volutpat non, tempor at orci. Aenean eu est metus, vel convallis augue. Integer aliquam turpis id neque posuere tincidunt. Morbi at ante felis, ac semper diam. Etiam iaculis sapien a mauris tempus semper. Nunc ut leo nec massa tincidunt sodales id sed justo. Fusce sed blandit turpis. Pellentesque scelerisque laoreet luctus.Integer lobortis sollicitudin sapien, vel consequat augue hendrerit ut. Quisque pretium, enim condimentum ornare viverra, est nisl convallis lacus, vel varius turpis mi a eros. Curabitur eget enim non quam condimentum lobortis id sit amet dolor. Aliquam in ligula ut nisi accumsan consectetur. Pellentesque blandit ultrices magna, eget posuere dui eleifend ut. Quisque ornare, ante eget iaculis rutrum, nisi dolor tincidunt diam, eget volutpat nibh sapien vel massa. Sed viverra pulvinar lectus et commodo. Sed lacus mauris, aliquet ut tristique at, feugiat sit amet lectus. Duis lacinia purus a lacus accumsan tristique. Donec felis nunc, egestas ut porttitor ac, vulputate at purus. Quisque euismod nibh sit amet metus ullamcorper a consequat ipsum luctus. Mauris vitae sapien tristique lorem mollis porta auctor vitae leo.Donec mauris nibh, elementum a tincidunt nec, volutpat vitae enim. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque at purus quis massa vulputate mollis ut eu risus. Cras libero diam, pharetra quis ultrices facilisis, tincidunt id arcu. Praesent at volutpat quam. Phasellus quis malesuada sapien. Nullam eu leo quam, bibendum sagittis turpis. Vestibulum sagittis accumsan enim ut tristique. Donec eu enim nisi. Curabitur mauris orci, fringilla et suscipit placerat, ullamcorper vel turpis. Mauris est arcu, gravida ac accumsan ac, tincidunt vel nibh. Mauris pulvinar massa tempor dui hendrerit vel elementum diam pulvinar. Sed felis leo, laoreet at laoreet nec, venenatis eu risus. Nunc a massa lacus. Vestibulum ut pellentesque sem. Donec consectetur, turpis consequat aliquam consectetur, leo nunc lobortis elit, at sagittis ipsum risus sed turpis. Vivamus at sollicitudin purus. Nulla facilisi.Morbi congue dignissim nibh ut commodo. Integer sed urna fermentum neque hendrerit aliquam ac et tortor. Phasellus massa sem, egestas sed tincidunt id, malesuada vel odio. Nulla arcu nisl, elementum at pretium interdum, pharetra at nulla. Vestibulum lectus ante, ultricies ac lacinia euismod, adipiscing at metus. Aliquam velit orci, posuere a dapibus dapibus, consectetur vitae est. Pellentesque suscipit pulvinar varius. Morbi sagittis blandit nulla, eu posuere arcu blandit eu. Nulla cursus nunc volutpat orci commodo aliquet. Quisque condimentum dui a mi facilisis tincidunt.Quisque ac nisi vitae ipsum condimentum luctus non quis erat. Pellentesque eget nunc sit amet ipsum pharetra vulputate ac in mi. Etiam consectetur nulla vitae risus pharetra a ornare ligula sagittis. Proin euismod mauris id lectus tincidunt luctus at sed ligula. Aenean porttitor hendrerit quam quis interdum. Suspendisse eu metus turpis, ut venenatis ipsum. Nulla ipsum orci, lacinia ac vestibulum ut, mattis quis eros. Pellentesque a velit et erat sodales ornare. Pellentesque pellentesque tortor eget magna facilisis accumsan. Praesent tincidunt posuere lacus sed sollicitudin. Praesent consectetur diam vel turpis elementum vehicula. Integer rutrum dignissim justo, ac dapibus quam fringilla non. Suspendisse porta elementum bibendum. Praesent at lorem at quam dignissim commodo. Etiam placerat euismod ipsum eu interdum. Aliquam in mi molestie justo varius venenatis eu aliquam quam. Integer feugiat, neque hendrerit egestas hendrerit, lectus ligula placerat enim, a faucibus ante ligula vitae mi. Praesent urna nunc, volutpat vitae scelerisque non, porta vel dolor. Aenean quam sem, dignissim at faucibus non, auctor sodales libero.Morbi justo elit, tempus ac cursus porta, eleifend nec odio. Ut nec quam velit. Duis elit nulla, placerat et rutrum quis, tempor non nisi. Quisque massa erat, pretium ut elementum sit amet, sodales vel metus. Phasellus sollicitudin sapien vel nisl faucibus ornare. Duis ut dolor arcu, vel semper felis. In quis orci mauris, id porttitor diam. Fusce ornare, dolor in posuere blandit, neque est mollis lacus, eu venenatis nisi orci vel diam. Etiam ornare sapien eu sapien tristique vitae consequat arcu consequat. Fusce a felis at dui sollicitudin pellentesque sit amet vel mi. Curabitur congue porttitor orci, eu auctor nibh mollis at. Nulla facilisi. Nam aliquet ullamcorper metus ac vulputate. Aenean feugiat purus vitae dui congue egestas. Curabitur sollicitudin leo eget nibh facilisis fermentum fermentum non nisi. Ut at libero eget est bibendum laoreet non eu ante. Proin pellentesque quam ut augue condimentum id suscipit risus tristique. Nunc laoreet risus elit.Suspendisse vel ante elit, eu feugiat diam. Etiam ultrices condimentum tempor. Donec eget porta lacus. Nunc in velit lacus, ut ultricies felis. Nulla id cursus metus. Praesent suscipit, orci a facilisis imperdiet, eros lectus eleifend nibh, sit amet auctor magna arcu ac enim. In malesuada volutpat felis a posuere. Aliquam mattis ipsum ut quam semper aliquet. Maecenas convallis consequat magna, blandit viverra nulla consectetur sit amet. Pellentesque id velit nec risus iaculis imperdiet ac accumsan metus. Suspendisse et sem massa. Vestibulum ac enim hendrerit est tincidunt ullamcorper.Curabitur placerat, libero id molestie vehicula, neque tortor vulputate sapien, ut blandit orci metus et nibh. Cras convallis justo a nibh iaculis ut convallis neque vestibulum. Duis gravida purus metus, nec viverra leo. In tincidunt elit sit amet orci lacinia non eleifend orci tincidunt. Praesent est metus, molestie a ullamcorper ultricies, cursus imperdiet augue. Sed vitae quam eu dui pellentesque dignissim. Curabitur convallis volutpat accumsan. Donec egestas malesuada est, vitae faucibus sem fermentum id. Donec porttitor rutrum vehicula. Nulla placerat risus in est egestas nec ultrices ligula auctor. Quisque erat lorem, ullamcorper vitae interdum sit amet, laoreet vel elit. Proin eu porta neque. Pellentesque eleifend, ipsum a sollicitudin ultrices, ligula nisl aliquet dui, eget fermentum quam mi vel.";
        return LOREM;
    }

    private void fillPathToUploadInput(String tempFileName) throws Exception {
        // create a valid path in upload input element. Instead of selecting a
        // file by some file browsing dialog, we use the local path directly.
        WebElement input = getInput();
        setLocalFileDetector(input);
        input.sendKeys(tempFileName);
    }

    private WebElement getSubmitButton() {
        UploadElement upload = $(UploadElement.class).first();
        WebElement submitButton = upload.findElement(By.className("v-button"));
        return submitButton;
    }

    private WebElement getInput() {
        return getDriver().findElement(By.className("gwt-FileUpload"));
    }

    private void setLocalFileDetector(WebElement element) throws Exception {
        if (getRunLocallyBrowser() != null) {
            return;
        }

        if (element instanceof WrapsElement) {
            element = ((WrapsElement) element).getWrappedElement();
        }
        if (element instanceof RemoteWebElement) {
            ((RemoteWebElement) element)
                    .setFileDetector(new LocalFileDetector());
        } else {
            throw new IllegalArgumentException(
                    "Expected argument of type RemoteWebElement, received "
                            + element.getClass().getName());
        }
    }
}
