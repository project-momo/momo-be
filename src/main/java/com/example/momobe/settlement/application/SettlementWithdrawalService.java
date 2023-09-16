package com.example.momobe.settlement.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.settlement.domain.enums.PointUsedType;
import com.example.momobe.settlement.domain.exception.CanNotWithdrawalException;
import com.example.momobe.settlement.domain.exception.NotFoundBankAccountException;
import com.example.momobe.settlement.dto.out.PointWithdrawalResponseDto;
import com.example.momobe.user.application.UserFindService;
import com.example.momobe.user.domain.NotEnoughPointException;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.momobe.common.exception.enums.ErrorCode.REQUEST_CONFLICT;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementWithdrawalService {
    private final UserFindService userFindService;
    private final UserRepository userRepository;

    public PointWithdrawalResponseDto deductPoint(Long userId, Long amount,boolean validAccount) {
        if(!validAccount) throw new NotFoundBankAccountException(ErrorCode.DATA_NOT_FOUND);
        try{
            User user = userFindService.verifyUser(userId);
            user.changeUserPoint(user.minusUserPoint(amount,PointUsedType.WITHDRAWAL));
            userRepository.save(user);
            PointWithdrawalResponseDto.WithdrawalDto withdrawalDto = new PointWithdrawalResponseDto.WithdrawalDto(true,amount,user.getUserPoint().getPoint());
            return PointWithdrawalResponseDto.of(withdrawalDto,validAccount);
        }catch (NotEnoughPointException e){
            throw new CanNotWithdrawalException(REQUEST_CONFLICT);
        }
    }
}
