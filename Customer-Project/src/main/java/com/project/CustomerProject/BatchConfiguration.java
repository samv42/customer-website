package com.project.CustomerProject;

import lombok.NonNull;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Bean
    public Job job(
            JobBuilderFactory jobBuilderFactory,
            Step titleStep,
            Step genreStep) {

        return jobBuilderFactory.get("book-loader-job")
                .incrementer(new RunIdIncrementer())
                .start(titleStep)
                .next(genreStep)
                .build();
    }

    @Bean(name = "titleStep")
    public Step titleStep(
            StepBuilderFactory stepBuilderFactory,
            ItemReader<Book> csvReader,
            TitleProcessor processor,
            BookWriter writer) {

        return stepBuilderFactory.get("title-step")
                .<Book, Book>chunk(100)
                .reader(csvReader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(false)
                .build();
    }

    @Bean
    public Step genreStep(
            StepBuilderFactory stepBuilderFactory,
            ItemReader<Book> repositoryReader,
            GenreProcessor processor,
            BookWriter writer) {

        // this step converts the designation into matching enum
        return stepBuilderFactory.get("genre-step")
                .<Book, Book>chunk(100)
                .reader(repositoryReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<Book> csvReader(
            @Value("${inputFile}") String inputFile) {

        return new FlatFileItemReaderBuilder<Book>()
                .name("csv-reader")
                .resource(new ClassPathResource(inputFile))
                .delimited()
                .names("book_title", "genre", "page_count")
                .linesToSkip(1)
                .fieldSetMapper(
                        new BeanWrapperFieldSetMapper<>() {
                            {setTargetType(Book.class);}
                        })
                .build();
    }

    @Bean
    public RepositoryItemReader<Book> repositoryReader(
            BookRepository bookRepository) {
        Map<String, Sort.Direction> codeMap = new HashMap<>();
        codeMap.put("title", Sort.Direction.ASC);
        return new RepositoryItemReaderBuilder<Book>()
                .repository(bookRepository)
                .methodName("findAll")
                .sorts(codeMap)
                .name("repository-reader")
                .build();
    }
    @Component
    public static class TitleProcessor implements
            ItemProcessor<Book, Book> {

        @Override
        public Book process(Book book) {
            String title = book.getTitle();
            int length = title.length();
            String p = "\"";
            if(title.charAt(length -1) == 'A' || title.charAt(length-1) == 'e' & title.charAt(length-2) == 'h' & title.charAt(length-3) == 'T'){
                int i = title.indexOf(",");
                String left = title.substring(0, i);
                String right = title.substring(i + 2, length);
                title = right + " " + left;
            }
            book.setTitle(title);
            return book;
        }
    }

    @Component
    public static class GenreProcessor implements
            ItemProcessor<Book, Book> {
        @Override
        public Book process(Book book) {
            book.setGenre(book.getGenre().toUpperCase());
            return book;
        }
    }

    @Component
    public static class BookWriter implements ItemWriter<Book> {

        @Autowired
        private BookRepository bookRepository;

        @Value("${sleepTime}")
        private Integer SLEEP_TIME;

        @Override
        public void write(List<? extends Book> books) throws InterruptedException {
            bookRepository.saveAll(books);
            Thread.sleep(SLEEP_TIME);
            System.out.println("Saved books: " + books);
        }
    }
}
