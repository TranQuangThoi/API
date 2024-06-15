package com.techmarket.api.mapper;

import com.techmarket.api.dto.account.AccountAutoCompleteDto;
import com.techmarket.api.dto.account.AccountDto;
import com.techmarket.api.dto.account.AccountForUser;
import com.techmarket.api.form.user.SignUpUserForm;
import com.techmarket.api.form.user.UpdateMyprofile;
import com.techmarket.api.form.user.UpdateUserForm;
import com.techmarket.api.model.Account;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {GroupMapper.class})
public interface AccountMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "group", target = "group", qualifiedByName = "fromEntityToGroupDto")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "avatarPath", target = "avatar")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @Mapping(source = "status",target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromAccountToDto")
    AccountDto fromAccountToDto(Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "avatarPath", target = "avatar")
    @Mapping(source = "isSuperAdmin", target = "isSuperAdmin")
    @Mapping(source = "status",target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromAccountForUser")
    AccountForUser fromAccountForUser(Account account);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @Mapping(source = "fullName", target = "fullName")
    @Named("fromAccountToAutoCompleteDto")
    AccountAutoCompleteDto fromAccountToAutoCompleteDto(Account account);


    @IterableMapping(elementTargetType = AccountAutoCompleteDto.class)
    List<AccountAutoCompleteDto> convertAccountToAutoCompleteDto(List<Account> list);

    @Mapping(source = "phone",target = "phone")
    @Mapping(source = "userName", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarPath", target = "avatarPath")
    @BeanMapping(ignoreByDefault = true)
    Account fromSignUpUserToAccount(SignUpUserForm signUpUserForm);


    @Mapping(source = "phone",target = "phone")
    @Mapping(source = "email",target = "email")
    @Mapping(source = "fullName",target = "fullName")
    @Mapping(source = "avatarPath",target = "avatarPath")
    @Mapping(source = "status",target = "status")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateUserFormToEntity(UpdateUserForm updateUserForm, @MappingTarget Account account );


    @Mapping(source = "phone",target = "phone")
    @Mapping(source = "email",target = "email")
    @Mapping(source = "fullName",target = "fullName")
    @Mapping(source = "avatarPath",target = "avatarPath")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateMyProfileToEntity(UpdateMyprofile updateMyprofile, @MappingTarget Account account );



}
