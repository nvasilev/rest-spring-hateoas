package org.jug.bg.rest.hateoas.spring.vote.resource;

import org.jug.bg.rest.hateoas.spring.common.resource.BadRequestException;
import org.jug.bg.rest.hateoas.spring.common.resource.NotFoundException;
import org.jug.bg.rest.hateoas.spring.vote.payload.VoteAssembler;
import org.jug.bg.rest.hateoas.spring.vote.payload.VotePayload;
import org.jug.bg.rest.hateoas.spring.vote.repository.VoteData;
import org.jug.bg.rest.hateoas.spring.vote.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ExposesResourceFor(VotePayload.class)
@RequestMapping("/polls/1/alternatives/1/votes")
public class VoteResource {

    @Autowired
    private VoteRepository repository;

    @Autowired
    private VoteAssembler assembler;

    @RequestMapping(method = RequestMethod.GET)
    public String getVotes() {
        return "hello world";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HttpEntity<VotePayload> getVote(@PathVariable("id") Long id) {
        VoteData voteData = repository.findVote(id);
        if (voteData == null) {
            throw new NotFoundException("Missing vote with id: " + id);
        }
        VotePayload payload = assembler.toResource(voteData);

        // TODO: add link to alternative via EntityLinks.linkToSingleResource(AlternativePayload.class, alternativeId)

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<VotePayload> getVote(@RequestBody
                                               VotePayload voteParam) { // FIXME: change request body!
        VoteData newVote = repository.createVote(voteParam.getEmail());

        VotePayload payload = assembler.toResource(newVote);

        // TODO: add link to alternative via EntityLinks.linkToSingleResource(AlternativePayload.class, alternativeId)

        return new ResponseEntity<>(payload, HttpStatus.CREATED);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFoundException(NotFoundException notFoundException) {
        handleError(notFoundException,
                    "Handling not found exception. A NOT FOUND error http status will be returned as response.");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handle(RuntimeException runtimeException) {
        handleError(runtimeException,
                    "Handling runtime exception. An INTERNAL SERVER error http status will be returned as response.");
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(BadRequestException badRequestException) {
        handleError(badRequestException,
                    "Handling bad request exception. A BAD REQUEST error http status will be returned as response.");
    }

    private void handleError(RuntimeException exception, String errMsg) {
        System.err.println(errMsg);
        exception.printStackTrace(System.err);
    }
}
